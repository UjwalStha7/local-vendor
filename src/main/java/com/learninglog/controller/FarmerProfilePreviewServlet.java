package com.learninglog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/farmerprofilepreview")
public class FarmerProfilePreviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("vendorName", "FreshHarvest Farms");
        req.setAttribute("storeName", "FreshHarvest Farms");
        FarmerProfileServlet.forwardProfile(req, resp, null, true);
    }
}
