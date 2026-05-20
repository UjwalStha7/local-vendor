package com.learninglog.util;

import com.learninglog.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class CustomerAuthUtil {

    private CustomerAuthUtil() {
    }

    public static User requireCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object userObj = SessionUtil.getAttribute(req, "user");
        Object roleObj = SessionUtil.getAttribute(req, "role");
        if (!(userObj instanceof User user) || !(roleObj instanceof String role)
                || !role.equalsIgnoreCase("customer")) {
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
