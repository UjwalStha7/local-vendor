package com.learninglog.dao;

import com.learninglog.model.VendorOrderRow;
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

public class VendorDashboardDaoImpl implements VendorDashboardDao {

    private static final DateTimeFormatter CHART_DAY_FMT =
            DateTimeFormatter.ofPattern("MMM d", Locale.US);

    @Override
    public Map<String, Object> fetchVendorKpis(int vendorUserId) {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("productsListed", countForVendor(vendorUserId, """
                SELECT COUNT(*) FROM products
                WHERE vendor_user_id = ? AND (is_active = 1 OR is_active IS NULL)
                """));
        stats.put("activeOrders", countForVendor(vendorUserId, """
                SELECT COUNT(DISTINCT o.id)
                FROM orders o
                INNER JOIN order_items oi ON oi.order_id = o.id
                INNER JOIN products p ON p.id = oi.product_id
                WHERE p.vendor_user_id = ?
                  AND o.status IN ('pending', 'confirmed', 'shipped')
                """));
        stats.put("pendingDeliveries", countForVendor(vendorUserId, """
                SELECT COUNT(DISTINCT o.id)
                FROM orders o
                INNER JOIN order_items oi ON oi.order_id = o.id
                INNER JOIN products p ON p.id = oi.product_id
                WHERE p.vendor_user_id = ?
                  AND o.status IN ('confirmed', 'shipped')
                """));
        stats.put("totalEarnings", sumForVendor(vendorUserId, """
                SELECT COALESCE(SUM(oi.quantity * oi.price_at_purchase), 0)
                FROM order_items oi
                INNER JOIN products p ON p.id = oi.product_id
                WHERE p.vendor_user_id = ?
                """));
        return stats;
    }

    @Override
    public List<WeeklyData> fetchWeeklyOrders(int vendorUserId) {
        Map<LocalDate, Integer> countsByDay = new HashMap<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT DATE(o.created_at) AS entry_date, COUNT(DISTINCT o.id) AS entry_count
                    FROM orders o
                    INNER JOIN order_items oi ON oi.order_id = o.id
                    INNER JOIN products p ON p.id = oi.product_id
                    WHERE p.vendor_user_id = ?
                      AND o.created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
                    GROUP BY DATE(o.created_at)
                    ORDER BY entry_date ASC
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        java.sql.Date sqlDate = rs.getDate("entry_date");
                        if (sqlDate != null) {
                            countsByDay.put(sqlDate.toLocalDate(), rs.getInt("entry_count"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor dashboard weekly orders: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return buildLastSevenDays(countsByDay);
    }

    @Override
    public List<VendorOrderRow> listRecentOrders(int vendorUserId, int limit) {
        List<VendorOrderRow> all = new VendorOrderDaoImpl().listOrdersForVendor(vendorUserId);
        int max = Math.max(1, limit);
        if (all.size() <= max) {
            return all;
        }
        return new ArrayList<>(all.subList(0, max));
    }

    private static List<WeeklyData> buildLastSevenDays(Map<LocalDate, Integer> countsByDay) {
        List<WeeklyData> series = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int daysAgo = 6; daysAgo >= 0; daysAgo--) {
            LocalDate day = today.minusDays(daysAgo);
            series.add(new WeeklyData(day.format(CHART_DAY_FMT), countsByDay.getOrDefault(day, 0)));
        }
        return series;
    }

    private static int countForVendor(int vendorUserId, String sql) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor dashboard kpi: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    private static double sumForVendor(int vendorUserId, String sql) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor dashboard earnings: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0.0;
    }
}
