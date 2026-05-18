package com.learninglog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Single product detail — {@code GET /productId?id=} */
@WebServlet("/productId")
public class ProductIdServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        if (id == null || id.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/customer/productId.jsp")
                .forward(req, resp);
    }
}
