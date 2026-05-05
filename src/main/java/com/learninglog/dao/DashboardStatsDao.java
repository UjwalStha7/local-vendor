package com.learninglog.dao;

import java.util.Map;

public interface DashboardStatsDao {
    Map<String, Integer> fetchKpiStats();
}
