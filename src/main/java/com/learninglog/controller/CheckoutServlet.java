package com.learninglog.controller;

import com.learninglog.dao.OrderDao;
import com.learninglog.dao.OrderDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.util.CustomerAuthUtil;
import com.learninglog.util.VendorOrderStatusUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final Pattern PRODUCT_ID = Pattern.compile("\"productId\"\\s*:\\s*(\\d+)");
    private static final Pattern QUANTITY = Pattern.compile("\"quantity\"\\s*:\\s*(\\d+)");

    private final OrderDao orderDao = new OrderDaoImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User customer = CustomerAuthUtil.requireCustomer(req, resp);
        if (customer == null) {
            return;
        }

        String body = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        List<OrderDao.CheckoutLine> lines = parseCheckoutBody(body);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json;charset=UTF-8");

        if (lines.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\":false,\"message\":\"No valid items. Each cart item needs a productId.\"}");
            return;
        }

        int orderId = orderDao.createOrder(customer.getId(), lines);
        if (orderId <= 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\":false,\"message\":\"Could not place order. Check stock and product availability.\"}");
            return;
        }

        String orderCode = VendorOrderStatusUtil.formatOrderId(orderId);
        resp.getWriter().write("{\"success\":true,\"orderId\":" + orderId
                + ",\"orderCode\":\"" + orderCode + "\"}");
    }

    private static List<OrderDao.CheckoutLine> parseCheckoutBody(String body) {
        List<OrderDao.CheckoutLine> lines = new ArrayList<>();
        if (body == null || body.isBlank()) {
            return lines;
        }

        Matcher idMatcher = PRODUCT_ID.matcher(body);
        Matcher qtyMatcher = QUANTITY.matcher(body);
        List<Integer> ids = new ArrayList<>();
        List<Integer> qtys = new ArrayList<>();
        while (idMatcher.find()) {
            ids.add(Integer.parseInt(idMatcher.group(1)));
        }
        while (qtyMatcher.find()) {
            qtys.add(Integer.parseInt(qtyMatcher.group(1)));
        }

        int count = Math.min(ids.size(), qtys.size());
        for (int i = 0; i < count; i++) {
            int qty = qtys.get(i);
            if (qty > 0) {
                lines.add(new OrderDao.CheckoutLine(ids.get(i), qty));
            }
        }
        return lines;
    }
}
