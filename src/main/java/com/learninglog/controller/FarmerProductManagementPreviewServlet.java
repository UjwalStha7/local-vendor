package com.learninglog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Preview Product Management UI without login (for development).
 */
@WebServlet(urlPatterns = {"/farmerproductmanagement", "/farmerproducts"})
public class FarmerProductManagementPreviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("vendorName", "FreshHarvest Farms");
        req.setAttribute("storeName", "FreshHarvest Farms");
        req.setAttribute("activeNav", "product-management");
        req.setAttribute("previewMode", Boolean.TRUE);
        req.getRequestDispatcher("/WEB-INF/views/farmer/product-management.jsp").forward(req, resp);
    }
}
