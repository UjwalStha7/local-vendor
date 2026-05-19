package com.learninglog.controller;

import com.learninglog.dao.UserDao;
import com.learninglog.dao.UserDaoImp;
import com.learninglog.entity.User;
import com.learninglog.util.LoginAuthUtil;
import com.learninglog.util.LoginAuthUtil.AccountType;
import com.learninglog.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImp();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = LoginAuthUtil.normalizeEmail(request.getParameter("email"));
        String password = request.getParameter("password");
        AccountType accountType = LoginAuthUtil.resolveAccountType(email);

        if (email.isEmpty() || password == null || password.isBlank()) {
            forwardWithError(request, response, "Please enter your email and password.", email);
            return;
        }

        User user = userDao.findByEmail(email);

        if (accountType == AccountType.ADMIN && user == null) {
            if (!LoginAuthUtil.isAdminBootstrapPassword(password)) {
                forwardWithError(request, response, "Invalid Email or Password", email);
                return;
            }
            user = bootstrapAdminAccount();
            if (user == null) {
                forwardWithError(request, response, "Unable to create admin account. Check database connection.", email);
                return;
            }
        }

        if (user == null) {
            String message = accountType == AccountType.VENDOR
                    ? "No farmer account found for this @krishak.np email. Contact support or complete vendor registration."
                    : "Invalid Email or Password";
            forwardWithError(request, response, message, email);
            return;
        }

        if (!PasswordUtil.checkpassword(password, user.getPassword())) {
            forwardWithError(request, response, "Invalid Email or Password", email);
            return;
        }

        if (!user.Isactive()) {
            forwardWithError(request, response, "Account is inactive", email);
            return;
        }

        if (!LoginAuthUtil.roleMatchesAccountType(user.getRole(), accountType)) {
            forwardWithError(request, response,
                    LoginAuthUtil.wrongAccountTypeMessage(accountType), email);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("role", LoginAuthUtil.sessionRoleFor(accountType));

        response.sendRedirect(request.getContextPath() + LoginAuthUtil.redirectPath(accountType));
    }

    private User bootstrapAdminAccount() {
        User admin = new User(
                "Krishak Admin",
                LoginAuthUtil.ADMIN_EMAIL,
                PasswordUtil.getHashpassword(LoginAuthUtil.ADMIN_DEFAULT_PASSWORD),
                ""
        );
        admin.setRole("admin");
        admin.setactive(true);
        if (!userDao.insertUser(admin)) {
            return userDao.findByEmail(LoginAuthUtil.ADMIN_EMAIL);
        }
        return userDao.findByEmail(LoginAuthUtil.ADMIN_EMAIL);
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String error, String email) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }
}
