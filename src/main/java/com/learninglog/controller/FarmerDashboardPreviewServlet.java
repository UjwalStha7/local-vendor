package com.learninglog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Temporary preview URL for the farmer dashboard UI without authentication.
 * Remove or protect before production.
 */
@WebServlet(name = "FarmerDashboardPreviewServlet", urlPatterns = {"/farmerdshboard"})
public class FarmerDashboardPreviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("vendorName", "Preview");
        req.setAttribute("previewMode", Boolean.TRUE);
        req.getRequestDispatcher("/WEB-INF/views/farmer/dashboard.jsp").forward(req, resp);
    }
}
