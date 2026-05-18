package com.learninglog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/farmerorderspreview")
public class FarmerOrdersPreviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("vendorName", "FreshHarvest Farms");
        req.setAttribute("storeName", "FreshHarvest Farms");
        FarmerOrdersServlet.forwardOrders(req, resp, true);
    }
}
