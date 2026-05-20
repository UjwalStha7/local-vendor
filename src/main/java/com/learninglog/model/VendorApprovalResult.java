package com.learninglog.model;

/**
 * Outcome of approving a farmer application — shown once to admin after approval.
 */
public class VendorApprovalResult {

    private final boolean success;
    private final String message;
    private final String vendorLoginEmail;
    private final String temporaryPassword;

    public VendorApprovalResult(boolean success, String message, String vendorLoginEmail, String temporaryPassword) {
        this.success = success;
        this.message = message;
        this.vendorLoginEmail = vendorLoginEmail;
        this.temporaryPassword = temporaryPassword;
    }

    public static VendorApprovalResult failure(String message) {
        return new VendorApprovalResult(false, message, null, null);
    }

    public static VendorApprovalResult ok(String accountEmail) {
        return new VendorApprovalResult(
                true,
                "Vendor approved. Their existing account role is now vendor. They sign in with the same email and password.",
                accountEmail,
                null
        );
    }

    public static VendorApprovalResult rejected() {
        return new VendorApprovalResult(true, "Application rejected.", null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getVendorLoginEmail() {
        return vendorLoginEmail;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }
}
