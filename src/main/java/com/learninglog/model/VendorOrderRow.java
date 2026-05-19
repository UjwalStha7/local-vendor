package com.learninglog.model;

/**
 * One row on the vendor Order Management table.
 */
public class VendorOrderRow {

    private final int dbOrderId;
    private final String orderId;
    private final String customerName;
    private final String phone;
    private final String orderDate;
    private final double total;
    private String status;

    public VendorOrderRow(int dbOrderId, String orderId, String customerName, String phone,
                          String orderDate, double total, String status) {
        this.dbOrderId = dbOrderId;
        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        this.orderDate = orderDate;
        this.total = total;
        this.status = status;
    }

    public int getDbOrderId() {
        return dbOrderId;
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

    public String getOrderDate() {
        return orderDate;
    }

    public double getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusKey() {
        if (status == null) {
            return "";
        }
        return status.trim().toLowerCase(java.util.Locale.ROOT);
    }
}
