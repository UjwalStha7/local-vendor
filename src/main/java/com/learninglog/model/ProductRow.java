package com.learninglog.model;

/**
 * Product row for vendor product management list.
 */
public class ProductRow {

    private final int id;
    private final String name;
    private final String category;
    private final String description;
    private final double price;
    private final String unit;
    private final int stock;
    private final String photoPath;
    private final boolean pendingModeration;

    public ProductRow(int id, String name, String category, String description, double price,
                      String unit, int stock, String photoPath, boolean pendingModeration) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description != null ? description : "";
        this.price = price;
        this.unit = unit;
        this.stock = stock;
        this.photoPath = photoPath;
        this.pendingModeration = pendingModeration;
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

    public boolean isPendingModeration() {
        return pendingModeration;
    }

    public String getPriceLabel() {
        return String.format("Rs. %.2f/%s", price, unit);
    }
}
