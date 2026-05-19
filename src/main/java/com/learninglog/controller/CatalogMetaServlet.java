package com.learninglog.controller;

import com.learninglog.dao.CatalogDao;
import com.learninglog.dao.CatalogDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@WebServlet("/api/catalog/meta")
public class CatalogMetaServlet extends HttpServlet {

    private final CatalogDao catalogDao = new CatalogDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<String> categories = catalogDao.listDistinctCategories();
        List<Map<String, String>> vendors = catalogDao.listVendors();

        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json;charset=UTF-8");

        PrintWriter out = resp.getWriter();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"categories\":[");
        for (int i = 0; i < categories.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append('"').append(escape(categories.get(i))).append('"');
        }
        sb.append("],\"vendors\":[");
        for (int i = 0; i < vendors.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            Map<String, String> v = vendors.get(i);
            sb.append("{\"name\":\"").append(escape(v.get("name")))
                    .append("\",\"slug\":\"").append(escape(v.get("slug"))).append("\"}");
        }
        sb.append("]}");
        out.write(sb.toString());
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
