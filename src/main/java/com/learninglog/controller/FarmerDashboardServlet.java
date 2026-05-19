package com.learninglog.controller;

import com.learninglog.entity.User;
import com.learninglog.util.FarmerAuthUtil;
import com.learninglog.util.VendorDashboardAttributes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "FarmerDashboardServlet", urlPatterns = {"/farmer/dashboard"})
public class FarmerDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        FarmerAuthUtil.setStoreAttributes(req, vendor);
        VendorDashboardAttributes.apply(req, vendor.getId());
        req.setAttribute("activeNav", "dashboard");
        req.setAttribute("topbarShowSearch", Boolean.FALSE);
        req.getRequestDispatcher("/WEB-INF/views/farmer/dashboard.jsp").forward(req, resp);
    }
}
