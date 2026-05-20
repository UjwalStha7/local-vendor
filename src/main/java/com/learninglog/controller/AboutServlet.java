package com.learninglog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/about")
public class AboutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                //throw new RuntimeException("Test 500 error"); // Test 500 error by uncommenting this line
        request.getRequestDispatcher("/WEB-INF/views/customer/about.jsp") //comment this check
                .forward(request, response); //comment this to check
    }
}
