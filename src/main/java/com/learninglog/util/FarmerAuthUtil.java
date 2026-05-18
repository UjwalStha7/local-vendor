package com.learninglog.util;

import com.learninglog.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public final class FarmerAuthUtil {

    private FarmerAuthUtil() {
    }

    public static User requireVendor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        Object userObj = session.getAttribute("user");
        String role = (String) session.getAttribute("role");
        if (!(userObj instanceof User user) || role == null || !role.equalsIgnoreCase("vendor")) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        return user;
    }

    public static void setStoreAttributes(HttpServletRequest req, User vendor) {
        String name = vendor.getUsername();
        if (name == null || name.isBlank()) {
            name = "FreshHarvest Farms";
        }
        req.setAttribute("vendorName", name);
        req.setAttribute("storeName", name);
    }
}
