package com.learninglog.model;

import java.util.List;

public class VendorOrderDetail {

    private final String orderId;
    private final String customerName;
    private final String phone;
    private final String email;
    private final String orderDate;
    private final String status;
    private final String statusKey;
    private final double vendorTotal;
    private final int totalQuantity;
    private final int lineCount;
    private final List<VendorOrderLineItem> items;

    public VendorOrderDetail(String orderId, String customerName, String phone, String email,
                             String orderDate, String status, String statusKey,
                             double vendorTotal, int totalQuantity, int lineCount,
                             List<VendorOrderLineItem> items) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.orderDate = orderDate;
        this.status = status;
        this.statusKey = statusKey;
        this.vendorTotal = vendorTotal;
        this.totalQuantity = totalQuantity;
        this.lineCount = lineCount;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusKey() {
        return statusKey;
    }

    public double getVendorTotal() {
        return vendorTotal;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getLineCount() {
        return lineCount;
    }

    public List<VendorOrderLineItem> getItems() {
        return items;
    }
}
