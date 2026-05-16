package com.learninglog.controller;

import com.learninglog.dao.VendorRequestDao;
import com.learninglog.dao.VendorRequestDaoImpl;
import com.learninglog.util.ValidationUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Landing-page mini form: collects email + phone into the same in-memory vendor queue
 * used by the admin dashboard (see {@link VendorRequestDao}).
 */
@WebServlet(name = "VendorLeadServlet", urlPatterns = {"/vendor-lead"})
public class VendorLeadServlet extends HttpServlet {

    private final VendorRequestDao vendorRequestDao = new VendorRequestDaoImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String email = trim(req.getParameter("email"));
        String phoneRaw = req.getParameter("phone");

        boolean okEmail = email != null && ValidationUtil.isValidEmail(email);
        String digits = phoneRaw == null ? "" : phoneRaw.replaceAll("\\D", "");
        boolean okPhone = digits.length() >= 10;
        String core10 = okPhone ? (digits.length() > 10 ? digits.substring(digits.length() - 10) : digits) : "";
        okPhone = okPhone && ValidationUtil.isValidPhoneNumber(core10);

        if (!okEmail || !okPhone) {
            resp.sendRedirect(req.getContextPath() + "/landing?leadErr=1");
            return;
        }

        String phoneDisplay = core10.substring(0, 5) + " " + core10.substring(5);
        vendorRequestDao.submitFarmerApplication("Krishak landing", "Vendor interest", email, phoneDisplay);
        resp.sendRedirect(req.getContextPath() + "/landing?leadOk=1");
    }

    private static String trim(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
