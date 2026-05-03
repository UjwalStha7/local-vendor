package com.learninglog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin area controller. Forwards to JSP views and exposes request attributes for future dynamic data.
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin", "/admin/"})
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String section = req.getParameter("section");
        if (section == null || section.isBlank()) {
            section = "dashboard";
        }
        req.setAttribute("activeNav", section);

        req.setAttribute("pageTitle", "Dashboard Overview");
        req.setAttribute(
                "pageSubtitle",
                "Welcome back! Here's what's happening with your marketplace.");

        req.setAttribute("adminDisplayName", "Admin");

        /* Dashboard stats — replace with service/DAO results later */
        req.setAttribute("statPendingRequests", 12);
        req.setAttribute("statActiveVendors", 48);
        req.setAttribute("statProductsListed", 324);
        req.setAttribute("statTotalOrders", 1247);

        /* Vendor requests bar chart (Mon–Sun), values 0–8 scale */
        req.setAttribute("vendorBarMon", 3);
        req.setAttribute("vendorBarTue", 5);
        req.setAttribute("vendorBarWed", 4);
        req.setAttribute("vendorBarThu", 6);
        req.setAttribute("vendorBarFri", 7);
        req.setAttribute("vendorBarSat", 5);
        req.setAttribute("vendorBarSun", 4);

        req.getRequestDispatcher("/WEB-INF/views/admin/admin.jsp").forward(req, resp);
    }
}
