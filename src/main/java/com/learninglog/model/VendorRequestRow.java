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
    private final String vendorLoginEmail;
    private Integer vendorUserId;

    public VendorRequestRow(int id, String email, String phone, String submittedAt, String status) {
        this(id, email, phone, submittedAt, status, "", "", null, null);
    }

    public VendorRequestRow(int id, String email, String phone, String submittedAt, String status,
                            String applicantName, String farmName) {
        this(id, email, phone, submittedAt, status, applicantName, farmName, null, null);
    }

    public VendorRequestRow(int id, String email, String phone, String submittedAt, String status,
                            String applicantName, String farmName, String vendorLoginEmail) {
        this(id, email, phone, submittedAt, status, applicantName, farmName, vendorLoginEmail, null);
    }

    public VendorRequestRow(int id, String email, String phone, String submittedAt, String status,
                            String applicantName, String farmName, String vendorLoginEmail,
                            Integer vendorUserId) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.submittedAt = submittedAt;
        this.status = status;
        this.applicantName = applicantName != null ? applicantName : "";
        this.farmName = farmName != null ? farmName : "";
        this.vendorLoginEmail = vendorLoginEmail;
        this.vendorUserId = vendorUserId;
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

    /** Waiting for admin (not yet approved or rejected). */
    public boolean isAwaitingAdminResponse() {
        return isPending() || "contacted".equalsIgnoreCase(status);
    }

    public boolean isApproved() {
        return "approved".equalsIgnoreCase(status);
    }

    /** Approved and still linked to an existing vendor user account. */
    public boolean hasActiveVendorLink() {
        return vendorUserId != null && vendorUserId > 0;
    }

    public boolean isRejected() {
        return "rejected".equalsIgnoreCase(status);
    }

    public boolean canApprove() {
        return isPending() || (isApproved() && !hasActiveVendorLink());
    }

    public void setVendorUserId(Integer vendorUserId) {
        this.vendorUserId = vendorUserId;
    }

    public String getVendorLoginEmail() {
        return vendorLoginEmail;
    }

    public Integer getVendorUserId() {
        return vendorUserId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getFarmName() {
        return farmName;
    }
}
