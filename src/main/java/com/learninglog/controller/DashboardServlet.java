package com.learninglog.controller;

import com.learninglog.dao.DashboardStatsDao;
import com.learninglog.dao.DashboardStatsDaoImpl;
import com.learninglog.dao.ModerationProductDao;
import com.learninglog.dao.ModerationProductDaoImpl;
import com.learninglog.dao.VendorAccountDao;
import com.learninglog.dao.VendorAccountDaoImpl;
import com.learninglog.dao.VendorRequestDao;
import com.learninglog.dao.VendorRequestDaoImpl;
import com.learninglog.model.ModerationProduct;
import com.learninglog.model.VendorAccountCard;
import com.learninglog.model.VendorApprovalResult;
import com.learninglog.model.VendorRequestRow;
import com.learninglog.model.WeeklyData;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/admin", "/admin/", "/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final DashboardStatsDao dashboardStatsDao = new DashboardStatsDaoImpl();
    private final VendorRequestDao vendorRequestDao = new VendorRequestDaoImpl();
    private final VendorAccountDao vendorAccountDao = new VendorAccountDaoImpl();
    private final ModerationProductDao moderationProductDao = new ModerationProductDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String section = req.getParameter("section");
        if (section == null) {
            section = "dashboard";
        }

        switch (section) {
            case "requests" -> forwardVendorRequests(req, resp);
            case "accounts" -> forwardVendorAccounts(req, resp);
            case "moderation" -> forwardModeration(req, resp);
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
        if ("moderateApprove".equals(action)) {
            applyModerationAction(req, true);
            redirectModeration(req, resp);
            return;
        }
        if ("moderateReject".equals(action)) {
            applyModerationAction(req, false);
            redirectModeration(req, resp);
            return;
        }
        if ("approve".equals(action) || "reject".equals(action)) {
            String idParam = req.getParameter("id");
            HttpSession session = req.getSession(true);
            if (idParam != null && !idParam.isBlank()) {
                try {
                    int requestId = Integer.parseInt(idParam);
                    VendorApprovalResult result = "reject".equals(action)
                            ? vendorRequestDao.rejectFarmerApplication(requestId)
                            : vendorRequestDao.approveFarmerApplication(requestId);
                    session.setAttribute("approvalFlash", result);
                } catch (NumberFormatException ignored) {
                    session.setAttribute("approvalFlash", VendorApprovalResult.failure("Invalid application id."));
                }
            }
            resp.sendRedirect(req.getContextPath() + "/admin?section=requests");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/admin");
    }

    private void forwardDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Integer> kpiStats = dashboardStatsDao.fetchKpiStats();
        List<WeeklyData> weeklyData = dashboardStatsDao.fetchWeeklyVendorApplications();

        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        for (WeeklyData wd : weeklyData) {
            dates.add(wd.getDate());
            counts.add(wd.getCount());
        }

        req.setAttribute("activeNav", "dashboard");
        req.setAttribute("pageTitle", "Dashboard Overview");
        req.setAttribute("pageSubtitle", "Welcome back! Here's what's happening with your marketplace.");

        req.setAttribute("statPendingRequests", kpiStats.getOrDefault("pendingRequests", 0));
        req.setAttribute("statActiveVendors", kpiStats.getOrDefault("activeVendors", 0));
        req.setAttribute("statProductsListed", kpiStats.getOrDefault("productsListed", 0));
        req.setAttribute("statTotalOrders", kpiStats.getOrDefault("totalOrders", 0));

        req.setAttribute("dates", dates);
        req.setAttribute("counts", counts);

        req.getRequestDispatcher("/WEB-INF/views/admin/admin.jsp").forward(req, resp);
    }

    private void forwardVendorRequests(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object flash = session.getAttribute("approvalFlash");
            if (flash instanceof VendorApprovalResult result) {
                req.setAttribute("approvalFlash", result);
                session.removeAttribute("approvalFlash");
            }
        }

        req.setAttribute("activeNav", "requests");
        req.setAttribute("vendorRequestRows", vendorRequestDao.listPendingVendorRequests());
        req.getRequestDispatcher("/WEB-INF/views/admin/vendor-requests.jsp").forward(req, resp);
    }

    private void forwardVendorAccounts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = req.getParameter("q");
        if (q == null) {
            q = "";
        }
        String trimmed = q.trim();
        List<VendorAccountCard> cards = vendorAccountDao.search(trimmed);
        req.setAttribute("activeNav", "accounts");
        req.setAttribute("vendorAccountsSearch", q);
        req.setAttribute("vendorAccountCards", cards);
        req.getRequestDispatcher("/WEB-INF/views/admin/vendor-accounts.jsp").forward(req, resp);
    }

    private void forwardModeration(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filter = moderationFilterOrDefault(req.getParameter("filter"));
        req.setAttribute("activeNav", "moderation");
        req.setAttribute("moderationFilter", filter);
        req.setAttribute("moderationProducts", moderationProductDao.listByFilter(filter));
        req.getRequestDispatcher("/WEB-INF/views/admin/product-moderation.jsp").forward(req, resp);
    }

    private void redirectModeration(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String filter = moderationFilterOrDefault(req.getParameter("filter"));
        resp.sendRedirect(req.getContextPath() + "/admin?section=moderation&filter=" + filter);
    }

    private void applyModerationAction(HttpServletRequest req, boolean approve) {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            if (approve) {
                moderationProductDao.approve(id);
            } else {
                moderationProductDao.reject(id);
            }
        } catch (NumberFormatException ignored) {
            // ignore
        }
    }

    private static String moderationFilterOrDefault(String raw) {
        if (raw == null || raw.isBlank()) {
            return "pending";
        }
        String f = raw.trim().toLowerCase(Locale.ROOT);
        if ("all".equals(f) || "pending".equals(f) || "approved".equals(f) || "rejected".equals(f)) {
            return f;
        }
        return "all";
    }

    private void forwardPlaceholder(HttpServletRequest req, HttpServletResponse resp, String nav, String title)
            throws ServletException, IOException {
        req.setAttribute("activeNav", nav);
        req.setAttribute("placeholderTitle", title);
        req.getRequestDispatcher("/WEB-INF/views/admin/admin-placeholder.jsp").forward(req, resp);
    }
}
