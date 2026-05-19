package com.learninglog.dao;

import com.learninglog.model.WeeklyData;
import com.learninglog.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardStatsDaoImpl implements DashboardStatsDao {

    private static final DateTimeFormatter CHART_DAY_FMT =
            DateTimeFormatter.ofPattern("MMM d", Locale.US);

    @Override
    public Map<String, Integer> fetchKpiStats() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("pendingRequests", countSingleValue(
                "SELECT COUNT(*) FROM vendor_requests WHERE status = 'pending'"));
        stats.put("activeVendors", countSingleValue(
                "SELECT COUNT(*) FROM users WHERE role = 'vendor' AND is_active = 1"));
        stats.put("productsListed", countSingleValue(
                "SELECT COUNT(*) FROM products WHERE is_active = 1 OR is_active IS NULL"));
        stats.put("totalOrders", countSingleValue("SELECT COUNT(*) FROM orders"));
        return stats;
    }

    @Override
    public List<WeeklyData> fetchWeeklyVendorApplications() {
        Map<LocalDate, Integer> countsByDay = new HashMap<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT DATE(created_at) AS entry_date, COUNT(*) AS entry_count
                    FROM vendor_requests
                    WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
                    GROUP BY DATE(created_at)
                    ORDER BY entry_date ASC
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date sqlDate = rs.getDate("entry_date");
                    if (sqlDate != null) {
                        countsByDay.put(sqlDate.toLocalDate(), rs.getInt("entry_count"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("dashboard weekly vendor applications: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return buildLastSevenDays(countsByDay);
    }

    private static List<WeeklyData> buildLastSevenDays(Map<LocalDate, Integer> countsByDay) {
        List<WeeklyData> series = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int daysAgo = 6; daysAgo >= 0; daysAgo--) {
            LocalDate day = today.minusDays(daysAgo);
            String label = day.format(CHART_DAY_FMT);
            int count = countsByDay.getOrDefault(day, 0);
            series.add(new WeeklyData(label, count));
        }
        return series;
    }

    private static int countSingleValue(String sql) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("dashboard kpi: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }
}
