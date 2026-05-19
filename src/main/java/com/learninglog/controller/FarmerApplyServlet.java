package com.learninglog.controller;

import com.learninglog.dao.VendorRequestDao;
import com.learninglog.dao.VendorRequestDaoImpl;
import com.learninglog.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FarmerApplyServlet", urlPatterns = {"/farmer/apply"})
public class FarmerApplyServlet extends HttpServlet {

    private final VendorRequestDao vendorRequestDao = new VendorRequestDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/farmer/apply.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String applicantName = trimToNull(req.getParameter("applicantName"));
        String farmName = trimToNull(req.getParameter("farmName"));
        String email = trimToNull(req.getParameter("email"));
        String phoneRaw = req.getParameter("phone");

        List<String> errors = new ArrayList<>();

        if (applicantName == null || applicantName.length() < 2) {
            errors.add("Please enter your full name (at least 2 characters).");
        } else if (applicantName.length() > 120) {
            errors.add("Name is too long.");
        }

        if (farmName == null || farmName.length() < 2) {
            errors.add("Please enter your farm or business name (at least 2 characters).");
        } else if (farmName.length() > 160) {
            errors.add("Farm or business name is too long.");
        }

        if (email == null || !ValidationUtil.isValidEmail(email)) {
            errors.add("Please enter a valid email address.");
        }

        String phoneDigits = phoneRaw == null ? "" : phoneRaw.replaceAll("\\D", "");
        if (phoneDigits.length() < 10) {
            errors.add("Please enter a phone number with at least 10 digits.");
        } else {
            String core10 = phoneDigits.length() > 10 ? phoneDigits.substring(phoneDigits.length() - 10) : phoneDigits;
            if (!ValidationUtil.isValidPhoneNumber(core10)) {
                errors.add("Phone number must be exactly 10 digits (use digits only, or include country code).");
            }
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("applicantName", nullToEmpty(req.getParameter("applicantName")));
            req.setAttribute("farmName", nullToEmpty(req.getParameter("farmName")));
            req.setAttribute("email", nullToEmpty(req.getParameter("email")));
            req.setAttribute("phone", nullToEmpty(req.getParameter("phone")));
            req.setAttribute("category", nullToEmpty(req.getParameter("category")));
            req.setAttribute("about", nullToEmpty(req.getParameter("about")));
            req.getRequestDispatcher("/WEB-INF/views/farmer/apply.jsp").forward(req, resp);
            return;
        }

        String core10 = phoneDigits.length() > 10 ? phoneDigits.substring(phoneDigits.length() - 10) : phoneDigits;
        String phoneDisplay = core10.substring(0, 5) + " " + core10.substring(5);

        vendorRequestDao.submitFarmerApplication(
                applicantName,
                farmName,
                email,
                phoneDisplay,
                nullToEmpty(req.getParameter("category")),
                nullToEmpty(req.getParameter("about"))
        );
        resp.sendRedirect(req.getContextPath() + "/farmer/apply?ok=1");
    }

    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
