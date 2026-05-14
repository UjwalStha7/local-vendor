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
import com.learninglog.model.VendorRequestRow;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/admin", "/admin/", "/dashboard"})
@MultipartConfig(
        fileSizeThreshold = 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 12 * 1024 * 1024
)
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
            case "add-vendor" -> forwardAddVendor(req, resp);
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
        String action = resolvePostAction(req);
        if ("saveVendor".equals(action)) {
            discardUploadedFile(req);
            resp.sendRedirect(req.getContextPath() + "/admin?section=add-vendor&ok=1");
            return;
        }
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

    private String resolvePostAction(HttpServletRequest req) throws IOException, ServletException {
        String direct = req.getParameter("action");
        if (direct != null && !direct.isBlank()) {
            return direct;
        }
        String ct = req.getContentType();
        if (ct == null || !ct.toLowerCase(Locale.ROOT).contains("multipart/form-data")) {
            return null;
        }
        for (Part part : req.getParts()) {
            if ("action".equals(part.getName())) {
                try (InputStream in = part.getInputStream()) {
                    return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
                }
            }
        }
        return null;
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

    private void forwardAddVendor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("activeNav", "add-vendor");
        req.getRequestDispatcher("/WEB-INF/views/admin/add-vendor.jsp").forward(req, resp);
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
            return "all";
        }
        String f = raw.trim().toLowerCase(Locale.ROOT);
        if ("all".equals(f) || "pending".equals(f) || "approved".equals(f) || "rejected".equals(f)) {
            return f;
        }
        return "all";
    }

    private void discardUploadedFile(HttpServletRequest req) {
        try {
            for (Part part : req.getParts()) {
                if ("documents".equals(part.getName()) && part.getSize() > 0) {
                    part.delete();
                }
            }
        } catch (Exception ignored) {
            // demo: no file storage
        }
    }

    private void forwardPlaceholder(HttpServletRequest req, HttpServletResponse resp, String nav, String title)
            throws ServletException, IOException {
        req.setAttribute("activeNav", nav);
        req.setAttribute("placeholderTitle", title);
        req.getRequestDispatcher("/WEB-INF/views/admin/admin-placeholder.jsp").forward(req, resp);
    }
}
