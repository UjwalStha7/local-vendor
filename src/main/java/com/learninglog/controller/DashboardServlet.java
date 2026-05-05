package com.learninglog.controller;

import com.learninglog.dao.DashboardStatsDao;
import com.learninglog.dao.DashboardStatsDaoImpl;
import com.learninglog.dao.VendorRequestDao;
import com.learninglog.dao.VendorRequestDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/admin", "/admin/", "/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final DashboardStatsDao dashboardStatsDao = new DashboardStatsDaoImpl();
    private final VendorRequestDao vendorRequestDao = new VendorRequestDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Integer> kpiStats = dashboardStatsDao.fetchKpiStats();
        List<String> labels = vendorRequestDao.fetchWeeklyLabels();
        List<Integer> data = vendorRequestDao.fetchWeeklyRequestCounts();

        req.setAttribute("activeNav", "dashboard");
        req.setAttribute("pageTitle", "Dashboard Overview");
        req.setAttribute("pageSubtitle", "Welcome back! Here's what's happening with your marketplace.");
        req.setAttribute("adminDisplayName", "Admin");

        req.setAttribute("statPendingRequests", kpiStats.getOrDefault("pendingRequests", 0));
        req.setAttribute("statActiveVendors", kpiStats.getOrDefault("activeVendors", 0));
        req.setAttribute("statProductsListed", kpiStats.getOrDefault("productsListed", 0));
        req.setAttribute("statTotalOrders", kpiStats.getOrDefault("totalOrders", 0));

        req.setAttribute("vendorRequestLabels", labels);
        req.setAttribute("vendorRequestData", data);

        req.getRequestDispatcher("/WEB-INF/views/admin/admin.jsp").forward(req, resp);
    }
}
