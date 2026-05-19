package com.learninglog.controller;

import com.learninglog.dao.ModerationProductDao;
import com.learninglog.dao.ModerationProductDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.util.FarmerAuthUtil;
import com.learninglog.util.ProductCategoryUtil;
import com.learninglog.util.ProductImageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/farmer/product-add"})
@MultipartConfig(
        fileSizeThreshold = 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class FarmerProductAddServlet extends HttpServlet {

    private final ModerationProductDao moderationDao = new ModerationProductDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        FarmerAuthUtil.setStoreAttributes(req, vendor);
        req.setAttribute("activeNav", "product-management");
        req.setAttribute("topbarShowSearch", Boolean.FALSE);
        req.setAttribute("productCategories", ProductCategoryUtil.CATEGORIES);
        req.setAttribute("pendingNewProduct", moderationDao.hasPendingNewProductRequest(vendor.getId()));
        if ("pending".equals(req.getParameter("error"))) {
            req.setAttribute("formError", "You already have a new product waiting for admin approval.");
        }
        req.getRequestDispatcher("/WEB-INF/views/farmer/product-add.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        req.setCharacterEncoding("UTF-8");

        String name = trim(req.getParameter("name"));
        String categoryRaw = trim(req.getParameter("category"));
        String category = ProductCategoryUtil.normalize(categoryRaw);
        String description = trim(req.getParameter("description"));
        String unit = trim(req.getParameter("unit"));

        List<String> errors = new ArrayList<>();
        double price = 0;
        int stock = 0;

        if (name == null || name.length() < 2) {
            errors.add("Product name is required (at least 2 characters).");
        }
        if (category == null) {
            errors.add("Please select a category.");
        }
        try {
            price = Double.parseDouble(req.getParameter("price"));
            if (price <= 0) {
                errors.add("Price must be greater than zero.");
            }
        } catch (NumberFormatException e) {
            errors.add("Enter a valid price.");
        }
        if (unit == null || unit.isBlank()) {
            errors.add("Unit is required (e.g. kg, piece).");
        }
        try {
            stock = Integer.parseInt(req.getParameter("stock"));
            if (stock < 0) {
                errors.add("Stock cannot be negative.");
            }
        } catch (NumberFormatException e) {
            errors.add("Enter a valid stock quantity.");
        }

        String photoPath = null;
        try {
            photoPath = ProductImageUtil.saveUploadedPhoto(req, "photo");
        } catch (IllegalArgumentException ex) {
            errors.add(ex.getMessage());
        } catch (ServletException ex) {
            errors.add("Could not read uploaded image.");
        }

        if (!errors.isEmpty()) {
            forwardWithForm(req, resp, vendor, errors, name, categoryRaw, description, unit, null,
                    req.getParameter("price"), req.getParameter("stock"));
            return;
        }

        try {
            moderationDao.submitNewProductRequest(
                    vendor.getId(), name, category, description, price, unit, stock, photoPath);
            resp.sendRedirect(req.getContextPath() + "/farmer/product-management?moderationSubmitted=1");
        } catch (IllegalStateException ex) {
            resp.sendRedirect(req.getContextPath() + "/farmer/product-add?error=pending");
        }
    }

    private void forwardWithForm(HttpServletRequest req, HttpServletResponse resp, User vendor,
                                 List<String> errors, String name, String category, String description,
                                 String unit, String photoPath, String price, String stock)
            throws IOException {
        try {
            req.setAttribute("errors", errors);
            req.setAttribute("formName", name != null ? name : "");
            req.setAttribute("formCategory", category != null ? category : "");
            req.setAttribute("formDescription", description != null ? description : "");
            req.setAttribute("formUnit", unit != null ? unit : "");
            req.setAttribute("formPhotoPath", photoPath != null ? photoPath : "");
            req.setAttribute("formPrice", price != null ? price : "");
            req.setAttribute("formStock", stock != null ? stock : "");
            FarmerAuthUtil.setStoreAttributes(req, vendor);
            req.setAttribute("activeNav", "product-management");
            req.setAttribute("topbarShowSearch", Boolean.FALSE);
            req.setAttribute("productCategories", ProductCategoryUtil.CATEGORIES);
            req.setAttribute("pendingNewProduct", moderationDao.hasPendingNewProductRequest(vendor.getId()));
            req.getRequestDispatcher("/WEB-INF/views/farmer/product-add.jsp").forward(req, resp);
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }

    private static String trim(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
