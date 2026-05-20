package com.learninglog.util;

import com.learninglog.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
