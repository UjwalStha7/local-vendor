package com.learninglog.model;

/** Vendor row for storefront filters (distinct sellers that list products). */
public class VendorOption {
    private final int vendorUserId;
    private final String displayName;

    public VendorOption(int vendorUserId, String displayName) {
        this.vendorUserId = vendorUserId;
        this.displayName = displayName;
    }

    public int getVendorUserId() {
        return vendorUserId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
