package com.learninglog.dao;

import com.learninglog.model.VendorApprovalResult;
import com.learninglog.model.VendorRequestRow;

import java.util.List;

public interface VendorRequestDao {
    List<VendorRequestRow> listVendorRequests();

    /** Applications awaiting admin approval (status = pending). */
    List<VendorRequestRow> listPendingVendorRequests();

    /**
     * True if a new vendor application should be rejected for this contact email:
     * an existing vendor user with this email, or a prior application already approved
     * (customer accounts may reuse their email to apply; they receive a separate vendor login when approved).
     */
    boolean isContactEmailAlreadyUsed(String email);

    /**
     * True if this contact email already has an application still under admin review
     * ({@code pending} or {@code contacted}).
     */
    boolean hasOpenVendorApplication(String email);

    /**
     * True when the most recent application for this contact email was rejected and they may submit again
     * (no open pending/contacted application for that email).
     */
    boolean isLatestVendorApplicationRejected(String email);

    void submitFarmerApplication(String applicantName, String farmName, String email, String phone,
                                 String category, String about);

    VendorApprovalResult approveFarmerApplication(int requestId);

    VendorApprovalResult rejectFarmerApplication(int requestId);

    /** When admin deletes a vendor user, clear linked applications so the contact can apply again. */
    void markRequestsRejectedForDeletedVendor(int vendorUserId);
}
