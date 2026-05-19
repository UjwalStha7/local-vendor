package com.learninglog.util;

import com.learninglog.dao.VendorProfileDao;
import com.learninglog.dao.VendorProfileDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.model.VendorProfile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class FarmerAuthUtil {

    private static final VendorProfileDao PROFILE_DAO = new VendorProfileDaoImpl();

    private FarmerAuthUtil() {
    }

    public static User requireVendor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object userObj = SessionUtil.getAttribute(req, "user");
        Object roleObj = SessionUtil.getAttribute(req, "role");
        if (!(userObj instanceof User user) || !(roleObj instanceof String role)
                || !role.equalsIgnoreCase("vendor")) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        return user;
    }

    public static void setStoreAttributes(HttpServletRequest req, User vendor) {
        String ctx = req.getContextPath();
        VendorProfile profile = PROFILE_DAO.loadForVendor(vendor);
        String name = profile.getShopName();
        if (name == null || name.isBlank()) {
            name = vendor.getUsername();
        }
        if (name == null || name.isBlank()) {
            name = "Your shop";
        }
        req.setAttribute("vendorName", name);
        req.setAttribute("storeName", name);
        req.setAttribute("storeLogo", profile.resolveLogoSrc(ctx));
    }
}
