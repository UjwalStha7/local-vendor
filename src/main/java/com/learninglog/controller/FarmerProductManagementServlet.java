package com.learninglog.controller;

import com.learninglog.dao.ModerationProductDao;
import com.learninglog.dao.ModerationProductDaoImpl;
import com.learninglog.dao.ProductDao;
import com.learninglog.dao.ProductDaoImpl;
import com.learninglog.entity.User;
import com.learninglog.util.FarmerAuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/farmer/product-management", "/farmer/products"})
public class FarmerProductManagementServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDaoImpl();
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

        String q = req.getParameter("q");
        req.setAttribute("productSearch", q != null ? q : "");
        req.setAttribute("vendorProducts", productDao.searchByVendor(vendor.getId(), q));
        req.setAttribute("pendingNewProduct", moderationDao.hasPendingNewProductRequest(vendor.getId()));

        if ("1".equals(req.getParameter("moderationSubmitted"))) {
            req.setAttribute("moderationNotice", "Your request was submitted for admin approval.");
        }
        if ("1".equals(req.getParameter("deleted"))) {
            req.setAttribute("moderationNotice", "Product deleted successfully.");
        }
        if ("0".equals(req.getParameter("deleted"))) {
            req.setAttribute("moderationNotice", "Could not delete product. It may not exist or has already been removed.");
        }

        req.getRequestDispatcher("/WEB-INF/views/farmer/product-management.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User vendor = FarmerAuthUtil.requireVendor(req, resp);
        if (vendor == null) {
            return;
        }
        if (!"delete".equals(req.getParameter("action"))) {
            resp.sendRedirect(req.getContextPath() + "/farmer/product-management");
            return;
        }
        int productId;
        try {
            productId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/farmer/product-management?deleted=0");
            return;
        }
        boolean deleted = productDao.deleteByIdForVendor(productId, vendor.getId());
        String q = req.getParameter("q");
        String redirect = req.getContextPath() + "/farmer/product-management?deleted=" + (deleted ? "1" : "0");
        if (q != null && !q.isBlank()) {
            redirect += "&q=" + java.net.URLEncoder.encode(q.trim(), java.nio.charset.StandardCharsets.UTF_8);
        }
        resp.sendRedirect(redirect);
    }
}
