package com.learninglog.controller;

import com.learninglog.dao.CatalogDao;
import com.learninglog.dao.CatalogDaoImpl;
import com.learninglog.model.CatalogProduct;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@WebServlet("/api/products")
public class ProductCatalogServlet extends HttpServlet {

    private final CatalogDao catalogDao = new CatalogDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String q = req.getParameter("q");
        List<String> categories = splitParam(req.getParameter("cat"));
        List<String> vendors = splitParam(req.getParameter("vendor"));
        Double minPrice = parseDouble(req.getParameter("min"));
        Double maxPrice = parseDouble(req.getParameter("max"));
        String availability = req.getParameter("avail");
        String sortBy = req.getParameter("sort");
        int limit = parseInt(req.getParameter("limit"), 0);
        boolean inStockOnly = "1".equals(req.getParameter("inStockOnly"))
                || "true".equalsIgnoreCase(req.getParameter("inStockOnly"));

        List<CatalogProduct> products = catalogDao.search(
                q, categories, vendors, minPrice, maxPrice, availability, sortBy, limit, inStockOnly);

        String ctx = req.getContextPath();
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json;charset=UTF-8");
        writeJson(resp.getWriter(), products, ctx);
    }

    private static List<String> splitParam(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        List<String> values = new ArrayList<>();
        for (String part : raw.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty() && !"all".equalsIgnoreCase(trimmed)) {
                values.add(trimmed);
            }
        }
        return values;
    }

    private static int parseInt(String raw, int defaultValue) {
        if (raw == null || raw.isBlank()) {
            return defaultValue;
        }
        try {
            return Math.max(0, Integer.parseInt(raw.trim()));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static Double parseDouble(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void writeJson(PrintWriter out, List<CatalogProduct> products, String ctx) {
        StringBuilder sb = new StringBuilder(products.size() * 200 + 16);
        sb.append("{\"products\":[");
        for (int i = 0; i < products.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            CatalogProduct p = products.get(i);
            sb.append('{');
            sb.append("\"id\":").append(p.getId()).append(',');
            append(sb, "name", p.getName());
            append(sb, "category", p.getCategory());
            append(sb, "vendor", p.getVendorName());
            append(sb, "vendorSlug", p.getVendorSlug());
            sb.append("\"price\":").append(String.format(Locale.US, "%.2f", p.getPrice())).append(',');
            append(sb, "unit", p.getUnit());
            sb.append("\"stock\":").append(p.getStock()).append(',');
            append(sb, "image", p.resolveImageUrl(ctx));
            sb.append("\"inStock\":").append(p.isInStock()).append(',');
            sb.append("\"limited\":").append(p.isLimited()).append(',');
            sb.append("\"outOfStock\":").append(p.isOutOfStock());
            sb.append('}');
        }
        sb.append("]}");
        out.write(sb.toString());
    }

    private static void append(StringBuilder sb, String key, String value) {
        sb.append('"').append(key).append("\":\"")
                .append(escape(value == null ? "" : value))
                .append("\",");
    }

    private static String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
