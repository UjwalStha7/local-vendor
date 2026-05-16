package com.learninglog.controller;

import com.learninglog.dao.ProductDao;
import com.learninglog.dao.ProductDaoImpl;
import com.learninglog.entity.Product;
import com.learninglog.model.ProductBrowseParams;
import com.learninglog.model.VendorOption;
import com.learninglog.util.CartUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@WebServlet(name = "BrowseServlet", urlPatterns = {"/browse"})
public class BrowseServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductBrowseParams params = parseParams(req);

        int sellableCount = 0;
        List<Product> rows = List.of();
        List<VendorOption> vendors = List.of();
        int matching = 0;

        try {
            sellableCount = productDao.countSellableProducts();
            vendors = productDao.listVendorsWithProducts();
            matching = productDao.countBrowse(params);
            int totalPagesCalc = matching == 0 ? 1 : (int) Math.ceil(matching / (double) params.getPageSize());
            if (params.getPage() > totalPagesCalc) {
                params.setPage(totalPagesCalc);
            }
            rows = productDao.browse(params);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        int totalPages = matching == 0 ? 1 : (int) Math.ceil(matching / (double) params.getPageSize());

        req.setAttribute("browseParams", params);
        req.setAttribute("products", rows);
        req.setAttribute("vendorOptions", vendors);
        req.setAttribute("sellableCount", sellableCount);
        req.setAttribute("matchingCount", matching);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("cartCount", CartUtil.totalItems(CartUtil.getCart(req.getSession())));
        req.setAttribute("queryPrefix", buildQueryPrefix(params));

        req.getRequestDispatcher("/WEB-INF/views/store/browse.jsp").forward(req, resp);
    }

    private static String buildQueryPrefix(ProductBrowseParams p) {
        String q = buildQuery(p, false);
        return q.isEmpty() ? "" : q + "&";
    }

    private static String buildQuery(ProductBrowseParams p, boolean includePage) {
        StringJoiner j = new StringJoiner("&");
        add(j, "q", p.getSearch());
        add(j, "cat", p.getCategory());
        if (p.getMinPrice() != null && p.getMinPrice().compareTo(BigDecimal.ZERO) > 0) {
            add(j, "minPrice", p.getMinPrice().toPlainString());
        }
        if (p.getMaxPrice() != null && p.getMaxPrice().compareTo(BigDecimal.ZERO) > 0) {
            add(j, "maxPrice", p.getMaxPrice().toPlainString());
        }
        if (p.getVendorUserIds() != null) {
            for (Integer vid : p.getVendorUserIds()) {
                add(j, "vendorId", String.valueOf(vid));
            }
        }
        add(j, "avail", p.getAvailability());
        add(j, "sort", p.getSort());
        if (includePage) {
            add(j, "page", String.valueOf(p.getPage()));
        }
        return j.toString();
    }

    private static void add(StringJoiner j, String key, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        j.add(URLEncoder.encode(key, StandardCharsets.UTF_8) + "="
                + URLEncoder.encode(value, StandardCharsets.UTF_8));
    }

    private static ProductBrowseParams parseParams(HttpServletRequest req) {
        ProductBrowseParams p = new ProductBrowseParams();

        String q = req.getParameter("q");
        p.setSearch(q != null ? q : "");

        String cat = req.getParameter("cat");
        if (cat == null || cat.isBlank()) {
            cat = "all";
        }
        cat = cat.trim().toLowerCase();
        if (!cat.equals("all") && !cat.equals("fruits") && !cat.equals("vegetables")) {
            cat = "all";
        }
        p.setCategory(cat);

        p.setMinPrice(parseMoney(req.getParameter("minPrice")));
        p.setMaxPrice(parseMoney(req.getParameter("maxPrice")));

        String[] vendorParams = req.getParameterValues("vendorId");
        List<Integer> vendorIds = new ArrayList<>();
        if (vendorParams != null) {
            for (String s : vendorParams) {
                if (s == null || s.isBlank()) {
                    continue;
                }
                try {
                    vendorIds.add(Integer.parseInt(s.trim()));
                } catch (NumberFormatException ignored) {
                    // skip invalid id
                }
            }
        }
        p.setVendorUserIds(vendorIds);

        String avail = req.getParameter("avail");
        if (avail == null || avail.isBlank()) {
            avail = "all";
        }
        avail = avail.trim().toLowerCase();
        if (!avail.equals("all") && !avail.equals("in") && !avail.equals("limited")) {
            avail = "all";
        }
        p.setAvailability(avail);

        String sort = req.getParameter("sort");
        if (sort == null || sort.isBlank()) {
            sort = "featured";
        }
        sort = sort.trim().toLowerCase();
        if (!sort.equals("featured") && !sort.equals("price-asc") && !sort.equals("price-desc")
                && !sort.equals("name-asc")) {
            sort = "featured";
        }
        p.setSort(sort);

        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isBlank()) {
            try {
                page = Integer.parseInt(pageParam.trim());
            } catch (NumberFormatException ignored) {
                page = 1;
            }
        }
        p.setPage(page);

        return p;
    }

    private static BigDecimal parseMoney(String raw) {
        if (raw == null) {
            return null;
        }
        String t = raw.trim();
        if (t.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
