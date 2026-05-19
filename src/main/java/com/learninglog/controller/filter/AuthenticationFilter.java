package com.learninglog.controller.filter;

import com.learninglog.entity.User;
import com.learninglog.util.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

/**
 * Global auth + role-based route protection (Week10AAD-style).
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final Set<String> PUBLIC_EXACT = Set.of(
            "/",
            "/login",
            "/register",
            "/logout",
            "/unauthorized",
            "/about",
            "/contact",
            "/product",
            "/productId",
            "/api/products",
            "/api/catalog/meta",
            "/cart",
            "/cart.html",
            "/farmer/apply"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String path = req.getRequestURI().substring(contextPath.length());
        if (path.isEmpty()) {
            path = "/";
        }

        if (isPublicAsset(path) || isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        boolean loggedIn = SessionUtil.isLoggedIn(req);
        boolean authPage = "/login".equals(path) || "/register".equals(path);

        if (!loggedIn) {
            if (authPage) {
                chain.doFilter(request, response);
                return;
            }
            res.sendRedirect(contextPath + "/login");
            return;
        }

        if (authPage) {
            res.sendRedirect(contextPath + homeForRole(sessionRole(req)));
            return;
        }

        String role = sessionRole(req);
        if (!isRoleAllowed(path, role)) {
            res.sendRedirect(contextPath + "/unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }

    private static boolean isPublicAsset(String path) {
        return path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/image/")
                || path.startsWith("/uploads/")
                || path.startsWith("/static/")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".jpeg")
                || path.endsWith(".webp")
                || path.endsWith(".ico")
                || path.endsWith(".svg");
    }

    private static boolean isPublicPath(String path) {
        if (PUBLIC_EXACT.contains(path)) {
            return true;
        }
        return path.startsWith("/index.jsp");
    }

    private static String sessionRole(HttpServletRequest req) {
        Object roleObj = SessionUtil.getAttribute(req, "role");
        if (roleObj instanceof String role && !role.isBlank()) {
            return role.trim().toLowerCase(Locale.ROOT);
        }
        Object userObj = SessionUtil.getAttribute(req, "user");
        if (userObj instanceof User user && user.getRole() != null) {
            return user.getRole().trim().toLowerCase(Locale.ROOT);
        }
        return "";
    }

    private static String homeForRole(String role) {
        return switch (role) {
            case "admin" -> "/admin/dashboard";
            case "vendor" -> "/farmer/dashboard";
            case "customer" -> "/customer/home";
            default -> "/login";
        };
    }

    private static boolean isRoleAllowed(String path, String role) {
        if ("admin".equals(role) && isAdminPath(path)) {
            return true;
        }
        if ("vendor".equals(role) && isVendorPath(path)) {
            return true;
        }
        if ("customer".equals(role) && isCustomerPath(path)) {
            return true;
        }
        return isSharedLoggedInPath(path);
    }

    private static boolean isAdminPath(String path) {
        return path.equals("/admin")
                || path.equals("/dashboard")
                || path.startsWith("/admin/");
    }

    private static boolean isVendorPath(String path) {
        if (path.startsWith("/farmer/") && !"/farmer/apply".equals(path)) {
            return true;
        }
        return path.startsWith("/vendor/");
    }

    private static boolean isCustomerPath(String path) {
        return path.startsWith("/customer/")
                || "/checkout".equals(path);
    }

    /** Endpoints any logged-in role may call (e.g. shared APIs). */
    private static boolean isSharedLoggedInPath(String path) {
        return false;
    }
}
