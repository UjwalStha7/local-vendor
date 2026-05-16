package com.learninglog.controller;

import com.learninglog.dao.ProductDao;
import com.learninglog.dao.ProductDaoImpl;
import com.learninglog.util.CartUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LandingServlet", urlPatterns = {"/landing"})
public class LandingServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("featuredProducts", productDao.findFeatured(6));
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        req.setAttribute("cartCount", CartUtil.totalItems(CartUtil.getCart(req.getSession())));
        req.getRequestDispatcher("/WEB-INF/views/store/landing.jsp").forward(req, resp);
    }
}
