package com.learninglog.controller;

import com.learninglog.dao.ModerationProductDao;
import com.learninglog.dao.ModerationProductDaoImpl;
import com.learninglog.dao.ProductDao;
import com.learninglog.dao.ProductDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.model.ProductRow;
import com.learninglog.util.FarmerAuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/farmer/product-edit"})
public class FarmerProductEditServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDaoImpl();
    private final ModerationProductDao moderationDao = new ModerationProductDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        int productId = parseProductId(req);
        if (productId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/farmer/product-management");
            return;
        }
        Optional<ProductRow> product = productDao.findByIdForVendor(productId, vendor.getId());
        if (product.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/farmer/product-management");
            return;
        }
        FarmerAuthUtil.setStoreAttributes(req, vendor);
        req.setAttribute("activeNav", "product-management");
        req.setAttribute("topbarShowSearch", Boolean.FALSE);
        req.setAttribute("product", product.get());
        req.setAttribute("pendingModeration", productDao.hasPendingModeration(productId));
        req.getRequestDispatcher("/WEB-INF/views/farmer/product-edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        req.setCharacterEncoding("UTF-8");
        int productId = parseProductId(req);
        if (productId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/farmer/product-management");
            return;
        }
        Optional<ProductRow> product = productDao.findByIdForVendor(productId, vendor.getId());
        if (product.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/farmer/product-management");
            return;
        }

        String name = trim(req.getParameter("name"));
        String category = trim(req.getParameter("category"));
        String description = trim(req.getParameter("description"));
        String unit = trim(req.getParameter("unit"));
        String photoPath = trim(req.getParameter("photoPath"));
        if (photoPath == null || photoPath.isBlank()) {
            photoPath = product.get().getPhotoPath();
        }

        List<String> errors = new ArrayList<>();
        double price = 0;
        int stock = 0;

        if (name == null || name.length() < 2) {
            errors.add("Product name is required (at least 2 characters).");
        }
        if (category == null || category.isBlank()) {
            errors.add("Category is required.");
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

        if (!errors.isEmpty()) {
            try {
                req.setAttribute("errors", errors);
                req.setAttribute("product", product.get());
                req.setAttribute("pendingModeration", productDao.hasPendingModeration(productId));
                req.setAttribute("formName", name != null ? name : "");
                req.setAttribute("formCategory", category != null ? category : "");
                req.setAttribute("formDescription", description != null ? description : "");
                req.setAttribute("formPrice", req.getParameter("price"));
                req.setAttribute("formUnit", unit != null ? unit : "");
                req.setAttribute("formStock", req.getParameter("stock"));
                req.setAttribute("formPhotoPath", photoPath);
                FarmerAuthUtil.setStoreAttributes(req, vendor);
                req.setAttribute("activeNav", "product-management");
                req.setAttribute("topbarShowSearch", Boolean.FALSE);
                req.getRequestDispatcher("/WEB-INF/views/farmer/product-edit.jsp").forward(req, resp);
            } catch (ServletException e) {
                throw new IOException(e);
            }
            return;
        }

        try {
            moderationDao.submitChangeRequest(
                    vendor.getId(),
                    product.get(),
                    name,
                    category,
                    description,
                    price,
                    unit,
                    stock,
                    photoPath
            );
            resp.sendRedirect(req.getContextPath() + "/farmer/product-management?moderationSubmitted=1");
        } catch (IllegalStateException ex) {
            resp.sendRedirect(req.getContextPath() + "/farmer/product-edit?id=" + productId + "&error=pending");
        }
    }

    private static int parseProductId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            return -1;
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
