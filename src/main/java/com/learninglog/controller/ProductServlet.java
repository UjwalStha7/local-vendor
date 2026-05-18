package com.learninglog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        if (id == null || id.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/shop");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/customer/product.jsp")
                .forward(req, resp);
    }
}
