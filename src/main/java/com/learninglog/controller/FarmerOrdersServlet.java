package com.learninglog.controller;

import com.learninglog.dao.VendorOrderDao;
import com.learninglog.dao.VendorOrderDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.model.VendorOrderRow;
import com.learninglog.util.FarmerAuthUtil;
import com.learninglog.util.FarmerOrderData;
import com.learninglog.util.VendorOrderStatusUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/farmer/orders")
public class FarmerOrdersServlet extends HttpServlet {

    private final VendorOrderDao orderDao = new VendorOrderDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        FarmerAuthUtil.setStoreAttributes(req, vendor);
        forwardOrders(req, resp, vendor.getId());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }

        String orderIdParam = req.getParameter("orderId");
        int orderDbId = VendorOrderStatusUtil.parseOrderId(orderIdParam);
        String uiStatus = FarmerOrderData.normalizeStatus(req.getParameter("status"));
        String dbStatus = VendorOrderStatusUtil.toDbStatus(uiStatus);

        if (orderDbId > 0) {
            orderDao.updateStatusForVendor(vendor.getId(), orderDbId, dbStatus);
        }

        String filter = req.getParameter("filter");
        if (filter == null || filter.isBlank()) {
            filter = "all";
        }
        resp.sendRedirect(req.getContextPath() + "/farmer/orders?filter=" + filter);
    }

    private void forwardOrders(HttpServletRequest req, HttpServletResponse resp, int vendorUserId)
            throws ServletException, IOException {
        req.setAttribute("activeNav", "orders");
        req.setAttribute("topbarShowSearch", Boolean.FALSE);

        List<VendorOrderRow> all = orderDao.listOrdersForVendor(vendorUserId);

        String filter = req.getParameter("filter");
        if (filter == null || filter.isBlank()) {
            filter = "all";
        }
        List<VendorOrderRow> visible = FarmerOrderData.filterByStatus(all, filter);

        Map<String, Integer> counts = FarmerOrderData.countByStatus(all);

        req.setAttribute("orders", visible);
        req.setAttribute("orderFilter", filter);
        req.setAttribute("statTotal", counts.get("total"));
        req.setAttribute("statPending", counts.get("pending"));
        req.setAttribute("statDispatched", counts.get("dispatched"));
        req.setAttribute("statDelivered", counts.get("delivered"));

        req.getRequestDispatcher("/WEB-INF/views/farmer/orders.jsp").forward(req, resp);
    }
}
