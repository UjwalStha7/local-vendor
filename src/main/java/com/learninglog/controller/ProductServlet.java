package com.learninglog.controller;

import com.learninglog.dao.ProductDao;
import com.learninglog.dao.ProductDaoImpl;
import com.learninglog.entity.Product;
import com.learninglog.util.CartUtil;
import com.learninglog.util.StorefrontUrls;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/browse");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/browse");
            return;
        }

        Product product;
        try {
            product = productDao.findById(id);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if (product == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        req.setAttribute("product", product);
        req.setAttribute("productImageUrl", StorefrontUrls.productImage(req, product.getPhotoPath()));
        req.setAttribute("cartCount", CartUtil.totalItems(CartUtil.getCart(req.getSession())));
        req.getRequestDispatcher("/product.jsp").forward(req, resp);
    }
}
