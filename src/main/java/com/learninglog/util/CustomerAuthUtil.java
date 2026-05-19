package com.learninglog.util;

import com.learninglog.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class CustomerAuthUtil {

    private CustomerAuthUtil() {
    }

    public static User requireCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            redirectToLogin(req, resp);
            return null;
        }
        Object userObj = session.getAttribute("user");
        String role = (String) session.getAttribute("role");
        if (!(userObj instanceof User user) || role == null || !role.equalsIgnoreCase("customer")) {
            redirectToLogin(req, resp);
            return null;
        }
        return user;
    }

    public static void redirectToLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
        String query = req.getQueryString();
        String target = path.substring(req.getContextPath().length());
        if (query != null && !query.isBlank()) {
            target = target + "?" + query;
        }
        String encoded = URLEncoder.encode(target, StandardCharsets.UTF_8);
        resp.sendRedirect(req.getContextPath() + "/login?redirect=" + encoded);
    }
}
