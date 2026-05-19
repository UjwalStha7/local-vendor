package com.learninglog.controller;

import com.learninglog.entity.User;
import com.learninglog.model.VendorOrderRow;
import com.learninglog.util.FarmerAuthUtil;
import com.learninglog.util.FarmerOrderData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/farmer/orders")
public class FarmerOrdersServlet extends HttpServlet {

    private static final String SESSION_ORDERS = "vendorPortalOrders";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        FarmerAuthUtil.setStoreAttributes(req, vendor);
        forwardOrders(req, resp, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }

        HttpSession session = req.getSession();
        List<VendorOrderRow> orders = getSessionOrders(session);
        String orderId = req.getParameter("orderId");
        String status = FarmerOrderData.normalizeStatus(req.getParameter("status"));
        if (orderId != null && !orderId.isBlank()) {
            for (VendorOrderRow row : orders) {
                if (row.getOrderId().equals(orderId.trim())) {
                    row.setStatus(status);
                    break;
                }
            }
        }

        String filter = req.getParameter("filter");
        if (filter == null || filter.isBlank()) {
            filter = "all";
        }
        resp.sendRedirect(req.getContextPath() + "/farmer/orders?filter=" + filter);
    }

    static void forwardOrders(HttpServletRequest req, HttpServletResponse resp, boolean preview)
            throws ServletException, IOException {
        req.setAttribute("activeNav", "orders");
        req.setAttribute("previewMode", preview);
        req.setAttribute("topbarShowSearch", Boolean.TRUE);
        req.setAttribute("topbarSearchAction",
                preview ? req.getContextPath() + "/farmerorderspreview" : req.getContextPath() + "/farmer/orders");
        req.setAttribute("topbarSearchPlaceholder", "Search orders...");

        List<VendorOrderRow> all = preview
                ? FarmerOrderData.allOrders()
                : getSessionOrders(req.getSession());
        String filter = req.getParameter("filter");
        if (filter == null || filter.isBlank()) {
            filter = "all";
        }
        String search = req.getParameter("q");
        List<VendorOrderRow> visible = FarmerOrderData.filter(all, filter, search);

        Map<String, Integer> counts = FarmerOrderData.countByStatus(all);

        req.setAttribute("orders", visible);
        req.setAttribute("orderFilter", filter);
        String searchValue = search == null ? "" : search;
        req.setAttribute("searchQuery", searchValue);
        req.setAttribute("topbarSearchValue", searchValue);
        req.setAttribute("statTotal", counts.get("total"));
        req.setAttribute("statPending", counts.get("pending"));
        req.setAttribute("statDispatched", counts.get("dispatched"));
        req.setAttribute("statDelivered", counts.get("delivered"));

        req.getRequestDispatcher("/WEB-INF/views/farmer/orders.jsp").forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    private static List<VendorOrderRow> getSessionOrders(HttpSession session) {
        Object stored = session.getAttribute(SESSION_ORDERS);
        if (stored instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof VendorOrderRow) {
            return (List<VendorOrderRow>) stored;
        }
        List<VendorOrderRow> fresh = FarmerOrderData.allOrders();
        session.setAttribute(SESSION_ORDERS, fresh);
        return fresh;
    }
}
