package com.learninglog.util;

import com.learninglog.entity.User;
import com.learninglog.model.VendorProfile;

public final class FarmerProfileData {

    private static final String SESSION_PROFILE = "vendorPortalProfile";

    private FarmerProfileData() {
    }

    public static VendorProfile defaultProfile() {
        VendorProfile p = new VendorProfile();
        p.setShopName("FreshHarvest Farms");
        p.setShopBio("");
        p.setEmail("contact@freshharvestfarms.com");
        p.setPhone("+1 (555) 123-4567");
        p.setAddress("");
        p.setLogoUrl("https://images.unsplash.com/photo-1540420773420-3366772f4999?w=200&h=200&fit=crop");
        p.setVerified(true);
        p.setMemberSince("January 2024");
        p.setResponseTime("Within 2 hours");
        return p;
    }

    public static VendorProfile fromUser(User user) {
        VendorProfile p = defaultProfile();
        if (user == null) {
            return p;
        }
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            p.setShopName(user.getUsername());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            p.setEmail(user.getEmail());
        }
        if (user.getPhone() != null && !user.getPhone().isBlank()) {
            p.setPhone(user.getPhone());
        }
        return p;
    }

    public static VendorProfile getSessionProfile(jakarta.servlet.http.HttpSession session, User vendor) {
        Object stored = session.getAttribute(SESSION_PROFILE);
        if (stored instanceof VendorProfile profile) {
            return profile;
        }
        VendorProfile fresh = fromUser(vendor);
        session.setAttribute(SESSION_PROFILE, fresh);
        return fresh;
    }

    public static void saveSessionProfile(jakarta.servlet.http.HttpSession session, VendorProfile profile) {
        session.setAttribute(SESSION_PROFILE, profile);
    }

    public static void applyFormParams(VendorProfile profile, jakarta.servlet.http.HttpServletRequest req) {
        profile.setShopName(trim(req.getParameter("shopName")));
        profile.setShopBio(trim(req.getParameter("shopBio")));
        profile.setEmail(trim(req.getParameter("email")));
        profile.setPhone(trim(req.getParameter("phone")));
        profile.setAddress(trim(req.getParameter("address")));
        profile.setLogoUrl(trim(req.getParameter("logoUrl")));
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
