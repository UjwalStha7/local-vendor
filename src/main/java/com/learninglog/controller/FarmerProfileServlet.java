package com.learninglog.controller;

import com.learninglog.entity.User;
import com.learninglog.model.VendorProfile;
import com.learninglog.util.FarmerAuthUtil;
import com.learninglog.util.FarmerProfileData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        forwardProfile(req, resp, vendor, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }

        HttpSession session = req.getSession();
        VendorProfile profile = FarmerProfileData.getSessionProfile(session, vendor);
        FarmerProfileData.applyFormParams(profile, req);
        FarmerProfileData.saveSessionProfile(session, profile);

        resp.sendRedirect(req.getContextPath() + "/farmer/profile?saved=1");
    }

    static void forwardProfile(HttpServletRequest req, HttpServletResponse resp, User vendor, boolean preview)
            throws ServletException, IOException {
        req.setAttribute("activeNav", "profile");
        req.setAttribute("previewMode", preview);

        VendorProfile profile = preview
                ? FarmerProfileData.defaultProfile()
                : FarmerProfileData.getSessionProfile(req.getSession(), vendor);

        boolean editMode = "1".equals(req.getParameter("edit"));
        req.setAttribute("profile", profile);
        req.setAttribute("editMode", editMode);
        req.setAttribute("vendorEmail", profile.getEmail());
        req.setAttribute("vendorPhone", profile.getPhone());
        req.setAttribute("storeName", profile.getShopName());

        req.getRequestDispatcher("/WEB-INF/views/farmer/profile.jsp").forward(req, resp);
    }
}
