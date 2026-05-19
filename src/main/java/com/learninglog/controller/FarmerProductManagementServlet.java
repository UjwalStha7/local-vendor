package com.learninglog.controller;

import com.learninglog.dao.ProductDao;
import com.learninglog.dao.ProductDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.util.FarmerAuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/farmer/product-management", "/farmer/products"})
public class FarmerProductManagementServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        FarmerAuthUtil.setStoreAttributes(req, vendor);
        req.setAttribute("activeNav", "product-management");
        req.setAttribute("previewMode", Boolean.FALSE);
        req.setAttribute("vendorProducts", productDao.listByVendor(vendor.getId()));
        if ("1".equals(req.getParameter("moderationSubmitted"))) {
            req.setAttribute("moderationNotice", "Your changes were submitted for admin approval.");
        }
        req.getRequestDispatcher("/WEB-INF/views/farmer/product-management.jsp").forward(req, resp);
    }
}
