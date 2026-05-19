package com.learninglog.controller;

import com.learninglog.dao.VendorRequestDao;
import com.learninglog.dao.VendorRequestDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FarmerApplyServlet", urlPatterns = {"/farmer/apply"})
public class FarmerApplyServlet extends HttpServlet {

    private final VendorRequestDao vendorRequestDao = new VendorRequestDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String statusEmail = resolveStatusCheckEmail(req);
        setApplicationStatusAttributes(req, statusEmail);
        if (trimToNull(req.getParameter("email")) == null && statusEmail != null) {
            req.setAttribute("email", statusEmail);
        }
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
            forwardWithErrors(req, resp, errors);
            return;
        }

        if (vendorRequestDao.hasOpenVendorApplication(email)) {
            errors.add("Your vendor application is already in process. Please wait for an admin to approve or reject it before applying again.");
            forwardWithErrors(req, resp, errors);
            return;
        }

        if (vendorRequestDao.isContactEmailAlreadyUsed(email)) {
            errors.add("You already have an approved vendor application for this email, or this email is registered as a vendor account. "
                    + "Sign in with your vendor login or contact support if you need help.");
            forwardWithErrors(req, resp, errors);
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

    private void forwardWithErrors(HttpServletRequest req, HttpServletResponse resp, List<String> errors)
            throws ServletException, IOException {
        req.setAttribute("errors", errors);
        String email = nullToEmpty(req.getParameter("email"));
        req.setAttribute("applicantName", nullToEmpty(req.getParameter("applicantName")));
        req.setAttribute("farmName", nullToEmpty(req.getParameter("farmName")));
        req.setAttribute("email", email);
        req.setAttribute("phone", nullToEmpty(req.getParameter("phone")));
        req.setAttribute("category", nullToEmpty(req.getParameter("category")));
        req.setAttribute("about", nullToEmpty(req.getParameter("about")));
        setApplicationStatusAttributes(req, trimToNull(email));
        req.getRequestDispatcher("/WEB-INF/views/farmer/apply.jsp").forward(req, resp);
    }

    private static String resolveStatusCheckEmail(HttpServletRequest req) {
        String param = trimToNull(req.getParameter("email"));
        if (param != null && ValidationUtil.isValidEmail(param)) {
            return param;
        }
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        Object userObj = session.getAttribute("user");
        String role = (String) session.getAttribute("role");
        if (userObj instanceof User user && "customer".equalsIgnoreCase(role)) {
            String sessionEmail = user.getEmail();
            if (sessionEmail != null && !sessionEmail.isBlank() && ValidationUtil.isValidEmail(sessionEmail.trim())) {
                return sessionEmail.trim();
            }
        }
        return null;
    }

    private void setApplicationStatusAttributes(HttpServletRequest req, String email) {
        if (email == null) {
            return;
        }
        if (vendorRequestDao.isLatestVendorApplicationRejected(email)) {
            req.setAttribute("applicationRejected", Boolean.TRUE);
        }
    }
}
