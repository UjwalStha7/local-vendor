package com.learninglog.controller;

import com.learninglog.dao.ProductDao;
import com.learninglog.dao.ProductDaoImpl;
import com.learninglog.entity.Product;
import com.learninglog.model.CartLineItem;
import com.learninglog.util.CartUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.13");
    private static final BigDecimal DELIVERY_FLAT = new BigDecimal("150");

    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        LinkedHashMap<Integer, Integer> cart = CartUtil.getCart(session);
        normalizeCart(cart);

        List<CartLineItem> lines = buildLines(cart);
        MoneyTotals totals = calculateTotals(lines);

        req.setAttribute("cartLines", lines);
        req.setAttribute("subtotal", totals.subtotal());
        req.setAttribute("tax", totals.tax());
        req.setAttribute("delivery", totals.delivery());
        req.setAttribute("total", totals.total());
        req.setAttribute("cartCount", CartUtil.totalItems(cart));

        if (Boolean.TRUE.equals(session.getAttribute("cartThankYou"))) {
            req.setAttribute("thankYou", true);
            session.removeAttribute("cartThankYou");
        }

        req.getRequestDispatcher("/WEB-INF/views/store/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        LinkedHashMap<Integer, Integer> cart = CartUtil.getCart(session);
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        action = action.trim().toLowerCase();

        try {
            switch (action) {
                case "add" -> handleAdd(req, cart);
                case "update" -> handleUpdate(req, cart);
                case "remove" -> handleRemove(req, cart);
                case "checkout" -> handleCheckout(session, cart);
                default -> {
                    // ignore
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if ("checkout".equals(action)) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        resp.sendRedirect(safeRedirect(req, req.getParameter("redirect")));
    }

    private void handleAdd(HttpServletRequest req, LinkedHashMap<Integer, Integer> cart) throws SQLException {
        int productId = parsePositiveInt(req.getParameter("productId"), -1);
        int qty = parsePositiveInt(req.getParameter("quantity"), 1);
        if (productId < 0 || qty <= 0) {
            return;
        }
        Product p = productDao.findById(productId);
        if (p == null || p.getStockQuantity() <= 0) {
            return;
        }
        int cappedQty = Math.min(qty, p.getStockQuantity());
        int current = cart.getOrDefault(productId, 0);
        int next = Math.min(current + cappedQty, p.getStockQuantity());
        cart.put(productId, next);
    }

    private void handleUpdate(HttpServletRequest req, LinkedHashMap<Integer, Integer> cart) throws SQLException {
        int productId = parsePositiveInt(req.getParameter("productId"), -1);
        int qty = parsePositiveInt(req.getParameter("quantity"), 0);
        if (productId < 0) {
            return;
        }
        if (qty <= 0) {
            cart.remove(productId);
            return;
        }
        Product p = productDao.findById(productId);
        if (p == null || p.getStockQuantity() <= 0) {
            cart.remove(productId);
            return;
        }
        cart.put(productId, Math.min(qty, p.getStockQuantity()));
    }

    private void handleRemove(HttpServletRequest req, LinkedHashMap<Integer, Integer> cart) {
        int productId = parsePositiveInt(req.getParameter("productId"), -1);
        if (productId >= 0) {
            cart.remove(productId);
        }
    }

    private void handleCheckout(HttpSession session, LinkedHashMap<Integer, Integer> cart) {
        if (!cart.isEmpty()) {
            cart.clear();
            session.setAttribute("cartThankYou", true);
        }
    }

    private static String safeRedirect(HttpServletRequest req, String raw) {
        String ctx = req.getContextPath();
        if (raw == null || raw.isBlank()) {
            return ctx + "/cart";
        }
        String r = raw.trim();
        if (r.contains("://") || r.startsWith("//")) {
            return ctx + "/cart";
        }
        if (r.contains("\r") || r.contains("\n")) {
            return ctx + "/cart";
        }
        if (!r.startsWith("/")) {
            r = "/" + r;
        }
        return ctx + r;
    }

    private void normalizeCart(LinkedHashMap<Integer, Integer> cart) {
        cart.entrySet().removeIf(e -> e.getValue() == null || e.getValue() <= 0);
        for (Map.Entry<Integer, Integer> e : new ArrayList<>(cart.entrySet())) {
            try {
                Product p = productDao.findById(e.getKey());
                if (p == null || p.getStockQuantity() <= 0) {
                    cart.remove(e.getKey());
                    continue;
                }
                int capped = Math.min(e.getValue(), p.getStockQuantity());
                if (capped <= 0) {
                    cart.remove(e.getKey());
                } else if (capped != e.getValue()) {
                    cart.put(e.getKey(), capped);
                }
            } catch (SQLException ex) {
                cart.remove(e.getKey());
            }
        }
    }

    private List<CartLineItem> buildLines(LinkedHashMap<Integer, Integer> cart) {
        List<CartLineItem> lines = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : cart.entrySet()) {
            try {
                Product p = productDao.findById(e.getKey());
                if (p == null) {
                    continue;
                }
                int q = e.getValue();
                if (q > 0) {
                    lines.add(new CartLineItem(p, q));
                }
            } catch (SQLException ex) {
                cart.remove(e.getKey());
            }
        }
        return lines;
    }

    private MoneyTotals calculateTotals(List<CartLineItem> lines) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartLineItem line : lines) {
            subtotal = subtotal.add(BigDecimal.valueOf(line.getLineTotal()));
        }
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal delivery = lines.isEmpty() ? BigDecimal.ZERO : DELIVERY_FLAT;
        BigDecimal total = subtotal.add(tax).add(delivery).setScale(2, RoundingMode.HALF_UP);
        return new MoneyTotals(subtotal.setScale(2, RoundingMode.HALF_UP), tax, delivery, total);
    }

    private static int parsePositiveInt(String raw, int fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private record MoneyTotals(BigDecimal subtotal, BigDecimal tax, BigDecimal delivery, BigDecimal total) {
    }
}
