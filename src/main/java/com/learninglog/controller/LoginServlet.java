package com.learninglog.controller;

import com.learninglog.dao.UserDao;
import com.learninglog.dao.UserDaoImp;
import com.learninglog.entity.User;
import com.learninglog.util.CookieUtil;
import com.learninglog.util.LoginAuthUtil;
import com.learninglog.util.PasswordUtil;
import com.learninglog.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImp();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email =
                LoginAuthUtil.normalizeEmail(request.getParameter("email"));

        String password = request.getParameter("password");

        if (email.isEmpty() || password == null || password.isBlank()) {

            forwardWithError(request, response, "Please enter email and password.", email);
            return;
        }

        User user = userDao.findByEmail(email);

        if (user == null) {
            forwardWithError(request, response, "Invalid email or password.", email);
            return;
        }

        if (!PasswordUtil.checkpassword(password, user.getPassword())) {
            forwardWithError(request, response, "Invalid email or password.", email);
            return;
        }

        if (!user.Isactive()) {
            forwardWithError(request, response, "Account is inactive.", email);
            return;
        }

        String role = LoginAuthUtil.normalizeRole(user.getRole());
        if (LoginAuthUtil.ADMIN_EMAIL.equals(email) && !"admin".equals(role)) {
            forwardWithError(request, response, "Invalid email or password.", email);
            return;
        }

        SessionUtil.setAttribute(request, "user", user);
        SessionUtil.setAttribute(request, "role", role);
        SessionUtil.setAttribute(request, "hasVendorProfile", userDao.hasVendorProfile(user.getId()));

        String username =
                (user.getUsername() == null
                        || user.getUsername().isBlank())
                        ? user.getEmail()
                        : user.getUsername();

        CookieUtil.addCookie(
                response,
                "username",
                username,
                24 * 60 * 60
        );

        response.sendRedirect(
                request.getContextPath() + LoginAuthUtil.redirectPathForRole(role)
        );
    }

    private void forwardWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            String error,
            String email)
            throws ServletException, IOException {

        request.setAttribute("error", error);
        request.setAttribute("email", email);

        request.getRequestDispatcher(
                "/WEB-INF/views/login.jsp"
        ).forward(request, response);
    }
}