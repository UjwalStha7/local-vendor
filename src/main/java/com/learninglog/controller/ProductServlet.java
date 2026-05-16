package com.learninglog.controller;

import com.learninglog.entity.Product;
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

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/shop.html");
            return;
        }

        int id = Integer.parseInt(idParam);

        // TODO: Replace with real DAO once ProductDao is built
        Product product = findById(id);

        if (product == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        req.setAttribute("product", product);
        req.getRequestDispatcher("/product.jsp").forward(req, resp);
    }

    // Temporary stub — remove when ProductDao is ready
    private Product findById(int id) {
        return new Product("Fresh Tomato", "Vegetables", 120, "kg", 50,
                           true, "Farmer Ram", "image/fresh_tomato.png", "Sweet and ripe.");
    }
}