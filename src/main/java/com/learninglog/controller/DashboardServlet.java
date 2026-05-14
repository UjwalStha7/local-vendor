package com.learninglog.controller;

import com.learninglog.dao.DashboardStatsDao;
import com.learninglog.dao.DashboardStatsDaoImpl;
import com.learninglog.dao.VendorRequestDao;
import com.learninglog.dao.VendorRequestDaoImpl;
import com.learninglog.model.VendorRequestRow;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/admin", "/admin/", "/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final DashboardStatsDao dashboardStatsDao = new DashboardStatsDaoImpl();
    private final VendorRequestDao vendorRequestDao = new VendorRequestDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String section = req.getParameter("section");
        if (section == null) {
            section = "dashboard";
        }

        switch (section) {
            case "requests" -> forwardVendorRequests(req, resp);
            case "add-vendor" -> forwardPlaceholder(req, resp, "add-vendor", "Add new Vendor");
            case "accounts" -> forwardPlaceholder(req, resp, "accounts", "Vendor Accounts");
            case "moderation" -> forwardPlaceholder(req, resp, "moderation", "Product Moderation");
            case "signout" -> {
                resp.sendRedirect(req.getContextPath() + "/");
                return;
            }
            default -> forwardDashboard(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("contact".equals(action)) {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                try {
                    vendorRequestDao.markVendorRequestContacted(Integer.parseInt(idParam));
                } catch (NumberFormatException ignored) {
                    // ignore bad id
                }
            }
            String filter = req.getParameter("filter");
            if (filter == null || filter.isBlank()) {
                filter = "all";
            }
            if (!"all".equals(filter) && !"pending".equals(filter) && !"contacted".equals(filter)) {
                filter = "all";
            }
            resp.sendRedirect(req.getContextPath() + "/admin?section=requests&filter=" + filter);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin");
    }

    private void forwardDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Integer> kpiStats = dashboardStatsDao.fetchKpiStats();
        List<String> labels = vendorRequestDao.fetchWeeklyLabels();
        List<Integer> data = vendorRequestDao.fetchWeeklyRequestCounts();

        req.setAttribute("activeNav", "dashboard");
        req.setAttribute("pageTitle", "Dashboard Overview");
        req.setAttribute("pageSubtitle", "Welcome back! Here's what's happening with your marketplace.");

        req.setAttribute("statPendingRequests", kpiStats.getOrDefault("pendingRequests", 0));
        req.setAttribute("statActiveVendors", kpiStats.getOrDefault("activeVendors", 0));
        req.setAttribute("statProductsListed", kpiStats.getOrDefault("productsListed", 0));
        req.setAttribute("statTotalOrders", kpiStats.getOrDefault("totalOrders", 0));

        req.setAttribute("vendorRequestLabels", labels);
        req.setAttribute("vendorRequestData", data);

        req.getRequestDispatcher("/WEB-INF/views/admin/admin.jsp").forward(req, resp);
    }

    private void forwardVendorRequests(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filter = req.getParameter("filter");
        if (filter == null || filter.isBlank()) {
            filter = "all";
        }
        if (!"all".equals(filter) && !"pending".equals(filter) && !"contacted".equals(filter)) {
            filter = "all";
        }

        List<VendorRequestRow> all = vendorRequestDao.listVendorRequests();
        List<VendorRequestRow> rows = new ArrayList<>();
        for (VendorRequestRow row : all) {
            if ("all".equals(filter)) {
                rows.add(row);
            } else if ("pending".equals(filter) && row.isPending()) {
                rows.add(row);
            } else if ("contacted".equals(filter) && !row.isPending()) {
                rows.add(row);
            }
        }

        req.setAttribute("activeNav", "requests");
        req.setAttribute("requestFilter", filter);
        req.setAttribute("vendorRequestRows", rows);
        req.getRequestDispatcher("/WEB-INF/views/admin/vendor-requests.jsp").forward(req, resp);
    }

    private void forwardPlaceholder(HttpServletRequest req, HttpServletResponse resp, String nav, String title)
            throws ServletException, IOException {
        req.setAttribute("activeNav", nav);
        req.setAttribute("placeholderTitle", title);
        req.getRequestDispatcher("/WEB-INF/views/admin/admin-placeholder.jsp").forward(req, resp);
    }
}
