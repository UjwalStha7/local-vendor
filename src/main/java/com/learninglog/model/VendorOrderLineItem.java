package com.learninglog.model;

public class VendorOrderLineItem {

    private final String productName;
    private final String unit;
    private final int quantity;
    private final double unitPrice;
    private final double lineTotal;

    public VendorOrderLineItem(String productName, String unit, int quantity, double unitPrice) {
        this.productName = productName;
        this.unit = unit;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = unitPrice * quantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getUnit() {
        return unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }
}
