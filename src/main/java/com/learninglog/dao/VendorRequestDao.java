package com.learninglog.dao;

import com.learninglog.model.VendorApprovalResult;
import com.learninglog.model.VendorRequestRow;

import java.util.List;

public interface VendorRequestDao {
    List<String> fetchWeeklyLabels();

    List<Integer> fetchWeeklyRequestCounts();

    List<VendorRequestRow> listVendorRequests();

    /** Applications awaiting admin approval (status = pending). */
    List<VendorRequestRow> listPendingVendorRequests();

    /** True if this contact email already has a user account or a pending/approved application. */
    boolean isContactEmailAlreadyUsed(String email);

    void submitFarmerApplication(String applicantName, String farmName, String email, String phone,
                                 String category, String about);

    VendorApprovalResult approveFarmerApplication(int requestId);

    VendorApprovalResult rejectFarmerApplication(int requestId);
}
