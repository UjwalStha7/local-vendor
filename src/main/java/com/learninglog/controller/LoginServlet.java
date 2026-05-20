package com.learninglog.controller;

import com.learninglog.dao.UserDao;
import com.learninglog.dao.UserDaoImp;
import com.learninglog.entity.User;
import com.learninglog.util.CookieUtil;
import com.learninglog.util.LoginAuthUtil;
import com.learninglog.util.LoginAuthUtil.AccountType;
import com.learninglog.util.PasswordUtil;
import com.learninglog.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImp();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String redirect = sanitizeRedirect(
                request.getParameter("redirect")
        );

        if (redirect != null) {
            request.setAttribute("redirect", redirect);
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = LoginAuthUtil.normalizeEmail(
                request.getParameter("email")
        );

        String password = request.getParameter("password");

        AccountType accountType =
                LoginAuthUtil.resolveAccountType(email);

        // Validation
        if (email.isEmpty() || password == null || password.isBlank()) {

            forwardWithError(request, response, "Please enter your email and password.", email);
            return;
        }

        // Find user
        User user = userDao.findByEmail(email);

        // User not found
        if (user == null) {

            String message =
                    accountType == AccountType.VENDOR
                            ? "No farmer account found for this @krishak.np email. Contact support or complete vendor registration."
                            : "Invalid Email or Password";

            forwardWithError(request, response, message, email);
            return;
        }

        // Password validation
        if (!PasswordUtil.checkpassword(password, user.getPassword())) {

            forwardWithError(request, response, "Invalid Email or Password", email);
            return;
        }

        // Account active check
        if (!user.Isactive()) {

            forwardWithError(request, response, "Account is inactive", email);
            return;
        }

        // Role validation
        if (!LoginAuthUtil.roleMatchesAccountType(user.getRole(), accountType)) {

            forwardWithError(request, response,
                    LoginAuthUtil.wrongAccountTypeMessage(accountType),
                    email
            );
            return;
        }

        // Session setup
        SessionUtil.setAttribute(request, "user", user);

        SessionUtil.setAttribute(request, "role", LoginAuthUtil.sessionRoleFor(accountType));

        // Remember username in cookie
        String rememberName = user.getUsername();

        if (rememberName == null || rememberName.isBlank()) {
            rememberName = user.getEmail();
        }

        CookieUtil.addCookie(response, "username", rememberName, 24 * 60 * 60);

        // Redirect handling
        String redirect = sanitizeRedirect(request.getParameter("redirect"));

        if (redirect != null && accountType == AccountType.CUSTOMER) {

            response.sendRedirect(request.getContextPath() + redirect);
            return;
        }

        response.sendRedirect(request.getContextPath() + LoginAuthUtil.redirectPath(accountType)
        );
    }

    private static String sanitizeRedirect(String raw) {

        if (raw == null || raw.isBlank()) {
            return null;
        }

        String path = raw.trim();

        if (!path.startsWith("/") || path.startsWith("//")) {
            return null;
        }

        return path;
    }

    private void forwardWithError(HttpServletRequest request,
            HttpServletResponse response,
            String error,
            String email
    ) throws ServletException, IOException {request.setAttribute("error", error);
        request.setAttribute("email", email);

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}