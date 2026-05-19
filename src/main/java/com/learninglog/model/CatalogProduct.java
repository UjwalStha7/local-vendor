package com.learninglog.model;

import com.learninglog.util.ImageUtil;

/**
 * Product shown on the customer shop / product page.
 */
public class CatalogProduct {

    private final int id;
    private final String name;
    private final String category;
    private final String description;
    private final String vendorName;
    private final String vendorSlug;
    private final double price;
    private final String unit;
    private final int stock;
    private final String photoPath;

    public CatalogProduct(int id, String name, String category, String description,
                          String vendorName, String vendorSlug, double price, String unit,
                          int stock, String photoPath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description != null ? description : "";
        this.vendorName = vendorName;
        this.vendorSlug = vendorSlug;
        this.price = price;
        this.unit = unit;
        this.stock = stock;
        this.photoPath = photoPath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorSlug() {
        return vendorSlug;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public int getStock() {
        return stock;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public boolean isOutOfStock() {
        return stock <= 0;
    }

    public boolean isLimited() {
        return stock > 0 && stock <= 5;
    }

    public boolean isInStock() {
        return stock > 5;
    }

    public String resolveImageUrl(String contextPath) {
        String url = ImageUtil.buildImageUrl(contextPath, photoPath);
        if (url != null) {
            return url;
        }
        String ctx = contextPath == null ? "" : contextPath;
        return ctx + "/image/fresh_apple.png";
    }

    public static String toVendorSlug(String vendorName) {
        if (vendorName == null || vendorName.isBlank()) {
            return "vendor";
        }
        return vendorName.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
