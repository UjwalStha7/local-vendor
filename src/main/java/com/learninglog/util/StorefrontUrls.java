package com.learninglog.util;

import jakarta.servlet.http.HttpServletRequest;

public final class StorefrontUrls {

    private StorefrontUrls() {
    }

    public static String productImage(HttpServletRequest req, String photoPath) {
        String base = req.getContextPath();
        if (photoPath == null || photoPath.isBlank()) {
            return base + "/image/hero.svg";
        }
        String p = photoPath.trim();
        if (p.startsWith("http://") || p.startsWith("https://")) {
            return p;
        }
        return base + "/" + p.replaceFirst("^/+", "");
    }
}
