package com.learninglog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if(session == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }

        String role = (String) session.getAttribute("role");

        if(role == null || !role.equalsIgnoreCase("admin")){

            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/dashboard.jsp"
        ).forward(request,response);
    }
}