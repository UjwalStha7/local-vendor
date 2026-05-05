package com.learninglog.dao;

import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardStatsDaoImpl implements DashboardStatsDao {

    @Override
    public Map<String, Integer> fetchKpiStats() {
        // Dummy values. Replace with DB-backed aggregation queries.
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("pendingRequests", 12);
        stats.put("activeVendors", 48);
        stats.put("productsListed", 324);
        stats.put("totalOrders", 1247);
        return stats;
    }
}
