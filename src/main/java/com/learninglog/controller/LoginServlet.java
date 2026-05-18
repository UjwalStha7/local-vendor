package com.learninglog.controller;
import jakarta.servlet.http.HttpSession;
import com.learninglog.dao.UserDao;
import com.learninglog.dao.UserDaoImp;
import com.learninglog.entity.User;
import com.learninglog.util.PasswordUtil;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

//   using email to find user
        User user = userDao.findByEmail(email);
        if (user == null) {
            request.setAttribute("error", "Invalid Email or Password");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        // Verify password by comparing with db pass
        boolean isPasswordCorrect = PasswordUtil.checkpassword(password, user.getPassword());

        if (!isPasswordCorrect) {
            request.setAttribute("error", "Invalid Email or Password");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }


        if (!user.Isactive()) {

            request.setAttribute("error", "Account is inactive");

            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);

            return;
        }
        HttpSession session = request.getSession();

        session.setAttribute("user", user);
        session.setAttribute("role", user.getRole());

        if ("admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");

        }
        else if ("vendor".equalsIgnoreCase(user.getRole())) {

            response.sendRedirect(request.getContextPath() + "/farmer/dashboard");

        }
        else {
            response.sendRedirect(request.getContextPath() + "/customer/home");
        }


    }
}