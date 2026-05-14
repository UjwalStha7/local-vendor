package com.learninglog.model;

/**
 * One product card on the admin Product Moderation page.
 */
public class ModerationProduct {

    private final int id;
    private final String name;
    private final String vendor;
    private final String category;
    private final String priceLabel;
    private final String submittedAt;
    private final String imagePath;
    private String status;

    public ModerationProduct(int id, String name, String vendor, String category, String priceLabel,
                             String submittedAt, String imagePath, String status) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.category = category;
        this.priceLabel = priceLabel;
        this.submittedAt = submittedAt;
        this.imagePath = imagePath;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVendor() {
        return vendor;
    }

    public String getCategory() {
        return category;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }

    public boolean isApproved() {
        return "approved".equalsIgnoreCase(status);
    }

    public boolean isRejected() {
        return "rejected".equalsIgnoreCase(status);
    }
}
