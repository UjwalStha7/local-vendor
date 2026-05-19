package com.learninglog.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * HttpOnly cookie helpers (Week10AAD-style).
 * Values are URL-encoded so names with spaces and other characters are valid per RFC 6265.
 */
public final class CookieUtil {

    private CookieUtil() {
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        String encoded = encodeValue(value);
        Cookie cookie = new Cookie(name, encoded);
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        addCookie(response, name, "", 0);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return decodeValue(cookie.getValue());
            }
        }
        return null;
    }

    private static String encodeValue(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String decodeValue(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
