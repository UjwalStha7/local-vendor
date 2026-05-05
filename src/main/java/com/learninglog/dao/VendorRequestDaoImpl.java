package com.learninglog.dao;

import java.util.Arrays;
import java.util.List;

public class VendorRequestDaoImpl implements VendorRequestDao {

    @Override
    public List<String> fetchWeeklyLabels() {
        // Dummy labels. Replace with date/category query output.
        return Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
    }

    @Override
    public List<Integer> fetchWeeklyRequestCounts() {
        // Dummy values. Replace with grouped count query output.
        return Arrays.asList(3, 5, 4, 6, 7, 5, 4);
    }
}
