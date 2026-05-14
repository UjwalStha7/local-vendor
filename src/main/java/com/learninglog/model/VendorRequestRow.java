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
    private final String applicantName;
    private final String farmName;

    public VendorRequestRow(int id, String email, String phone, String submittedAt, String status) {
        this(id, email, phone, submittedAt, status, "", "");
    }

    public VendorRequestRow(int id, String email, String phone, String submittedAt, String status,
                            String applicantName, String farmName) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.submittedAt = submittedAt;
        this.status = status;
        this.applicantName = applicantName != null ? applicantName : "";
        this.farmName = farmName != null ? farmName : "";
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

    public String getApplicantName() {
        return applicantName;
    }

    public String getFarmName() {
        return farmName;
    }
}
