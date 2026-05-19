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

    private static final DateTimeFormatter ORDER_DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);

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
        List<VendorOrderRow> rows = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT o.id, o.status, o.created_at,
                           u.username AS customer_name,
                           COALESCE(u.phone, '') AS phone,
                           (
                               SELECT COALESCE(SUM(oi2.quantity * oi2.price_at_purchase), 0)
                               FROM order_items oi2
                               INNER JOIN products p2 ON p2.id = oi2.product_id
                               WHERE oi2.order_id = o.id AND p2.vendor_user_id = ?
                           ) AS vendor_total
                    FROM orders o
                    INNER JOIN users u ON u.id = o.user_id
                    WHERE EXISTS (
                        SELECT 1 FROM order_items oi
                        INNER JOIN products p ON p.id = oi.product_id
                        WHERE oi.order_id = o.id AND p.vendor_user_id = ?
                    )
                    ORDER BY o.created_at DESC
                    LIMIT ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                ps.setInt(2, vendorUserId);
                ps.setInt(3, Math.max(1, limit));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        java.sql.Timestamp created = rs.getTimestamp("created_at");
                        String orderDate = created != null
                                ? created.toLocalDateTime().toLocalDate().format(ORDER_DATE_FMT)
                                : "";
                        String status = mapOrderStatus(rs.getString("status"));
                        rows.add(new VendorOrderRow(
                                "ORD-" + String.format("%03d", rs.getInt("id")),
                                rs.getString("customer_name"),
                                rs.getString("phone"),
                                orderDate,
                                rs.getDouble("vendor_total"),
                                status
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor dashboard recent orders: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return rows;
    }

    private static String mapOrderStatus(String dbStatus) {
        if (dbStatus == null) {
            return "pending";
        }
        return switch (dbStatus.toLowerCase(Locale.ROOT)) {
            case "confirmed", "shipped" -> "dispatched";
            case "delivered" -> "delivered";
            case "cancelled" -> "cancelled";
            default -> "pending";
        };
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
