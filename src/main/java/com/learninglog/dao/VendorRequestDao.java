package com.learninglog.dao;

import com.learninglog.model.VendorRequestRow;

import java.util.List;

public interface VendorRequestDao {
    List<String> fetchWeeklyLabels();

    List<Integer> fetchWeeklyRequestCounts();

    List<VendorRequestRow> listVendorRequests();

    void markVendorRequestContacted(int id);

    void submitFarmerApplication(String applicantName, String farmName, String email, String phone);
}
