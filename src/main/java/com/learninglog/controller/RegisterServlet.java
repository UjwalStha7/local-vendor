package com.learninglog.controller;

import com.learninglog.dao.UserDao;
import com.learninglog.dao.UserDaoImp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private  final UserDao userDao = new UserDaoImp();

    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse  response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request,response);
    }


    @Override
    protected  void doPost(HttpServletRequest request , HttpServletResponse response)
                    throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

    }
}
