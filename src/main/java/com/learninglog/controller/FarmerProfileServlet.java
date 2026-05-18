package com.learninglog.controller;

import com.learninglog.entity.User;
import com.learninglog.util.FarmerAuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/farmer/profile")
public class FarmerProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        FarmerAuthUtil.setStoreAttributes(req, vendor);
        req.setAttribute("activeNav", "profile");
        req.setAttribute("vendorEmail", vendor.getEmail());
        req.setAttribute("vendorPhone", vendor.getPhone());
        req.getRequestDispatcher("/WEB-INF/views/farmer/profile.jsp").forward(req, resp);
    }
}
