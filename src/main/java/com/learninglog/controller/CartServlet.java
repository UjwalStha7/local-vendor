package com.learninglog.controller;

import com.learninglog.util.CustomerAuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Cart and checkout require a logged-in customer account.
 */
@WebServlet(urlPatterns = {"/cart", "/cart/"})
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (CustomerAuthUtil.requireCustomer(req, resp) == null) {
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/customer/cart.jsp").forward(req, resp);
    }
}
