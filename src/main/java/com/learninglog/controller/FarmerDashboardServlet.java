package com.learninglog.controller;

import com.learninglog.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "FarmerDashboardServlet", urlPatterns = {"/farmer/dashboard"})
public class FarmerDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User vendor = requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        req.setAttribute("vendorName", vendor.getUsername());
        req.getRequestDispatcher("/WEB-INF/views/farmer/dashboard.jsp").forward(req, resp);
    }

    private static User requireVendor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
}
