package com.learninglog.dao;

import com.learninglog.model.WeeklyData;

import java.util.List;
import java.util.Map;

public interface DashboardStatsDao {

    Map<String, Integer> fetchKpiStats();

    /** Vendor applications per day for the last 7 days (including zero-count days). */
    List<WeeklyData> fetchWeeklyVendorApplications();
}
