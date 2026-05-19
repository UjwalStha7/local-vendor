package com.learninglog.model;

/**
 * Product change request for admin moderation (before vs proposed).
 */
public class ModerationProduct {

    private final int requestId;
    private final int productId;
    private final String vendor;
    private final String submittedAt;
    private String status;

    private final String prevName;
    private final String prevCategory;
    private final String prevDescription;
    private final String prevPriceLabel;
    private final String prevUnit;
    private final int prevStock;
    private final String prevPhotoPath;

    private final String proposedName;
    private final String proposedCategory;
    private final String proposedDescription;
    private final String proposedPriceLabel;
    private final String proposedUnit;
    private final int proposedStock;
    private final String proposedPhotoPath;

    public ModerationProduct(int requestId, int productId, String vendor, String submittedAt, String status,
                             String prevName, String prevCategory, String prevDescription,
                             double prevPrice, String prevUnit, int prevStock, String prevPhotoPath,
                             String proposedName, String proposedCategory, String proposedDescription,
                             double proposedPrice, String proposedUnit, int proposedStock, String proposedPhotoPath) {
        this.requestId = requestId;
        this.productId = productId;
        this.vendor = vendor;
        this.submittedAt = submittedAt;
        this.status = status;
        this.prevName = prevName;
        this.prevCategory = prevCategory;
        this.prevDescription = prevDescription != null ? prevDescription : "";
        this.prevPriceLabel = formatPrice(prevPrice, prevUnit);
        this.prevUnit = prevUnit;
        this.prevStock = prevStock;
        this.prevPhotoPath = prevPhotoPath;
        this.proposedName = proposedName;
        this.proposedCategory = proposedCategory;
        this.proposedDescription = proposedDescription != null ? proposedDescription : "";
        this.proposedPriceLabel = formatPrice(proposedPrice, proposedUnit);
        this.proposedUnit = proposedUnit;
        this.proposedStock = proposedStock;
        this.proposedPhotoPath = proposedPhotoPath;
    }

    private static String formatPrice(double price, String unit) {
        return String.format("Rs. %.2f/%s", price, unit);
    }

    public int getRequestId() {
        return requestId;
    }

    public int getId() {
        return requestId;
    }

    public int getProductId() {
        return productId;
    }

    public String getVendor() {
        return vendor;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrevName() {
        return prevName;
    }

    public String getPrevCategory() {
        return prevCategory;
    }

    public String getPrevDescription() {
        return prevDescription;
    }

    public String getPrevPriceLabel() {
        return prevPriceLabel;
    }

    public String getPrevUnit() {
        return prevUnit;
    }

    public int getPrevStock() {
        return prevStock;
    }

    public String getPrevPhotoPath() {
        return prevPhotoPath;
    }

    public String getProposedName() {
        return proposedName;
    }

    public String getProposedCategory() {
        return proposedCategory;
    }

    public String getProposedDescription() {
        return proposedDescription;
    }

    public String getProposedPriceLabel() {
        return proposedPriceLabel;
    }

    public String getProposedUnit() {
        return proposedUnit;
    }

    public int getProposedStock() {
        return proposedStock;
    }

    public String getProposedPhotoPath() {
        return proposedPhotoPath;
    }

    /** Display image: proposed if set, else previous. */
    public String getImagePath() {
        if (proposedPhotoPath != null && !proposedPhotoPath.isBlank()) {
            return proposedPhotoPath;
        }
        return prevPhotoPath;
    }

    public boolean isNameChanged() {
        return !prevName.equalsIgnoreCase(proposedName);
    }

    public boolean isCategoryChanged() {
        return !prevCategory.equalsIgnoreCase(proposedCategory);
    }

    public boolean isDescriptionChanged() {
        return !prevDescription.equals(proposedDescription);
    }

    public boolean isPriceChanged() {
        return !prevPriceLabel.equals(proposedPriceLabel);
    }

    public boolean isStockChanged() {
        return prevStock != proposedStock;
    }

    public boolean isPhotoChanged() {
        String a = prevPhotoPath == null ? "" : prevPhotoPath;
        String b = proposedPhotoPath == null ? "" : proposedPhotoPath;
        return !a.equals(b);
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
