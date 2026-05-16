package com.learninglog.entity;

public class Product {

    private int id;
    private int vendorUserId;
    private String name;
    private String category;
    private String description;
    private double price;
    private String unit;
    private int stockQuantity;
    private String photoPath;
    private boolean active;
    /** Joined vendor label for storefront */
    private String vendorDisplayName;

    public Product() {
    }

    public Product(int id, int vendorUserId, String name, String category, String description,
                   double price, String unit, int stockQuantity, String photoPath, boolean active,
                   String vendorDisplayName) {
        this.id = id;
        this.vendorUserId = vendorUserId;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.unit = unit;
        this.stockQuantity = stockQuantity;
        this.photoPath = photoPath;
        this.active = active;
        this.vendorDisplayName = vendorDisplayName;
    }

    /** Legacy constructor used by older stubs */
    public Product(String name, String category, double price, String unit,
                   int stock, boolean organicIgnored, String farmerName,
                   String photoPath, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.unit = unit;
        this.stockQuantity = stock;
        this.photoPath = photoPath;
        this.description = description;
        this.vendorDisplayName = farmerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVendorUserId() {
        return vendorUserId;
    }

    public void setVendorUserId(int vendorUserId) {
        this.vendorUserId = vendorUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /** Alias for JSPs expecting {@code stock}. */
    public int getStock() {
        return stockQuantity;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getVendorDisplayName() {
        return vendorDisplayName;
    }

    public void setVendorDisplayName(String vendorDisplayName) {
        this.vendorDisplayName = vendorDisplayName;
    }

    /** Alias for older JSP fields */
    public String getFarmerName() {
        return vendorDisplayName;
    }

    public boolean isOrganic() {
        String n = name != null ? name.toLowerCase() : "";
        String d = description != null ? description.toLowerCase() : "";
        String c = category != null ? category.toLowerCase() : "";
        return n.contains("organic") || d.contains("organic") || c.contains("organic");
    }

    public boolean isLimitedStock() {
        return stockQuantity > 0 && stockQuantity <= 5;
    }
}
