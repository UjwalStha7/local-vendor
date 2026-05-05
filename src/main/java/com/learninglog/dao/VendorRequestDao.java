package com.learninglog.dao;

import java.util.List;

public interface VendorRequestDao {
    List<String> fetchWeeklyLabels();
    List<Integer> fetchWeeklyRequestCounts();
}
