package com.learninglog.controller;

import com.learninglog.dao.VendorOrderDao;
import com.learninglog.dao.VendorOrderDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.model.VendorOrderDetail;
import com.learninglog.model.VendorOrderLineItem;
import com.learninglog.util.FarmerAuthUtil;
import com.learninglog.util.VendorOrderStatusUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@WebServlet("/farmer/order-detail")
public class FarmerOrderDetailServlet extends HttpServlet {

    private final VendorOrderDao orderDao = new VendorOrderDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }

        int orderDbId = VendorOrderStatusUtil.parseOrderId(req.getParameter("orderId"));
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json;charset=UTF-8");

        if (orderDbId <= 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid order id\"}");
            return;
        }

        var detailOpt = orderDao.findOrderDetail(vendor.getId(), orderDbId);
        if (detailOpt.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Order not found\"}");
            return;
        }

        writeJson(resp.getWriter(), detailOpt.get());
    }

    private static void writeJson(PrintWriter out, VendorOrderDetail d) {
        StringBuilder sb = new StringBuilder(512);
        sb.append('{');
        appendField(sb, "orderId", d.getOrderId());
        appendField(sb, "customerName", d.getCustomerName());
        appendField(sb, "phone", d.getPhone());
        appendField(sb, "email", d.getEmail());
        appendField(sb, "orderDate", d.getOrderDate());
        appendField(sb, "status", d.getStatus());
        appendField(sb, "statusKey", d.getStatusKey());
        sb.append("\"vendorTotal\":").append(String.format(Locale.US, "%.2f", d.getVendorTotal())).append(',');
        sb.append("\"totalQuantity\":").append(d.getTotalQuantity()).append(',');
        sb.append("\"lineCount\":").append(d.getLineCount()).append(',');
        sb.append("\"items\":[");
        for (int i = 0; i < d.getItems().size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            VendorOrderLineItem item = d.getItems().get(i);
            sb.append('{');
            appendField(sb, "productName", item.getProductName());
            appendField(sb, "unit", item.getUnit());
            sb.append("\"quantity\":").append(item.getQuantity()).append(',');
            sb.append("\"unitPrice\":").append(String.format(Locale.US, "%.2f", item.getUnitPrice())).append(',');
            sb.append("\"lineTotal\":").append(String.format(Locale.US, "%.2f", item.getLineTotal()));
            sb.append('}');
        }
        sb.append("]}");
        out.write(sb.toString());
    }

    private static void appendField(StringBuilder sb, String key, String value) {
        sb.append('"').append(key).append("\":\"").append(escapeJson(value)).append("\",");
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
