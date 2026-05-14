package com.learninglog.model;

/**
 * One row on the admin Vendor Requests table.
 */
public class VendorRequestRow {

    private final int id;
    private final String email;
    private final String phone;
    private final String submittedAt;
    private String status;

    public VendorRequestRow(int id, String email, String phone, String submittedAt, String status) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.submittedAt = submittedAt;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
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

    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
}
