package com.learninglog.controller;

import com.learninglog.dao.UserDao;
import com.learninglog.dao.UserDaoImp;
import com.learninglog.entity.User;
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

        // Verify password
        boolean isPasswordCorrect =
                PasswordUtil.checkpassword(password, user.getPassword());

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

    }
}