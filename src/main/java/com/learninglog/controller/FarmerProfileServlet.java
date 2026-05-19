package com.learninglog.controller;

import com.learninglog.dao.VendorProfileDao;
import com.learninglog.dao.VendorProfileDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.model.VendorProfile;
import com.learninglog.util.FarmerAuthUtil;
import com.learninglog.util.FarmerProfileData;
import com.learninglog.util.ProductImageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/farmer/profile")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class FarmerProfileServlet extends HttpServlet {

    private final VendorProfileDao profileDao = new VendorProfileDaoImpl();

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

        VendorProfile existing = profileDao.loadForVendor(vendor);
        String shopName = trim(req.getParameter("shopName"));
        String shopBio = trim(req.getParameter("shopBio"));
        String phone = trim(req.getParameter("phone"));
        String address = trim(req.getParameter("address"));
        String logoPath = existing.getLogoUrl();

        try {
            String uploaded = ProductImageUtil.saveVendorLogo(req, "logo");
            if (uploaded != null) {
                logoPath = uploaded;
            }
        } catch (IllegalArgumentException ex) {
            resp.sendRedirect(req.getContextPath() + "/farmer/profile?edit=1&error="
                    + java.net.URLEncoder.encode(ex.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
            return;
        }

        if (shopName.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/farmer/profile?edit=1&error="
                    + java.net.URLEncoder.encode("Shop name is required.", java.nio.charset.StandardCharsets.UTF_8));
            return;
        }

        boolean saved = profileDao.saveProfile(vendor.getId(), shopName, shopBio, phone, address, logoPath);
        if (saved) {
            vendor.setPhone(phone);
            HttpSession session = req.getSession();
            session.setAttribute("user", vendor);
        }

        String redirect = saved
                ? "/farmer/profile?saved=1"
                : "/farmer/profile?edit=1&error="
                + java.net.URLEncoder.encode("Could not save profile. Please try again.", java.nio.charset.StandardCharsets.UTF_8);
        resp.sendRedirect(req.getContextPath() + redirect);
    }

    static void forwardProfile(HttpServletRequest req, HttpServletResponse resp, User vendor, boolean preview)
            throws ServletException, IOException {
        req.setAttribute("activeNav", "profile");
        req.setAttribute("previewMode", preview);
        req.setAttribute("topbarShowSearch", Boolean.FALSE);

        VendorProfile profile = preview
                ? FarmerProfileData.defaultProfile()
                : new VendorProfileDaoImpl().loadForVendor(vendor);

        boolean editMode = "1".equals(req.getParameter("edit"));
        req.setAttribute("profile", profile);
        req.setAttribute("editMode", editMode);
        req.setAttribute("vendorEmail", profile.getEmail());
        req.setAttribute("vendorPhone", profile.getPhone());
        req.setAttribute("storeName", profile.getShopName());
        req.setAttribute("storeLogo", profile.resolveLogoSrc(req.getContextPath()));

        String error = req.getParameter("error");
        if (error != null && !error.isBlank()) {
            req.setAttribute("profileError", error);
        }

        req.getRequestDispatcher("/WEB-INF/views/farmer/profile.jsp").forward(req, resp);
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
