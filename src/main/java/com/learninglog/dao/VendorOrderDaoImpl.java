package com.learninglog.dao;

import com.learninglog.model.VendorOrderDetail;
import com.learninglog.model.VendorOrderLineItem;
import com.learninglog.model.VendorOrderRow;
import com.learninglog.util.DatabaseConnection;
import com.learninglog.util.VendorOrderStatusUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class VendorOrderDaoImpl implements VendorOrderDao {

    private static final DateTimeFormatter ORDER_DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);

    private static final String VENDOR_ORDER_EXISTS = """
            SELECT 1 FROM order_items oi
            INNER JOIN products p ON p.id = oi.product_id
            WHERE oi.order_id = ? AND p.vendor_user_id = ?
            LIMIT 1
            """;

    @Override
    public List<VendorOrderRow> listOrdersForVendor(int vendorUserId) {
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
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                ps.setInt(2, vendorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rows.add(mapOrderRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor orders list: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return rows;
    }

    @Override
    public Optional<VendorOrderDetail> findOrderDetail(int vendorUserId, int orderDbId) {
        if (orderDbId <= 0) {
            return Optional.empty();
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (!vendorHasOrder(conn, vendorUserId, orderDbId)) {
                return Optional.empty();
            }

            String headerSql = """
                    SELECT o.id, o.status, o.created_at,
                           u.username AS customer_name,
                           COALESCE(u.phone, '') AS phone,
                           COALESCE(u.email, '') AS email
                    FROM orders o
                    INNER JOIN users u ON u.id = o.user_id
                    WHERE o.id = ?
                    """;
            int id;
            String dbStatus;
            String orderDate;
            String customerName;
            String phone;
            String email;

            try (PreparedStatement ps = conn.prepareStatement(headerSql)) {
                ps.setInt(1, orderDbId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    id = rs.getInt("id");
                    dbStatus = rs.getString("status");
                    Timestamp created = rs.getTimestamp("created_at");
                    orderDate = created != null
                            ? created.toLocalDateTime().toLocalDate().format(ORDER_DATE_FMT)
                            : "";
                    customerName = rs.getString("customer_name");
                    phone = rs.getString("phone");
                    email = rs.getString("email");
                }
            }

            List<VendorOrderLineItem> items = loadLineItems(conn, orderDbId, vendorUserId);
            double vendorTotal = 0;
            int totalQty = 0;
            for (VendorOrderLineItem item : items) {
                vendorTotal += item.getLineTotal();
                totalQty += item.getQuantity();
            }

            String displayStatus = VendorOrderStatusUtil.toDisplayStatus(dbStatus);
            return Optional.of(new VendorOrderDetail(
                    VendorOrderStatusUtil.formatOrderId(id),
                    customerName,
                    phone,
                    email,
                    orderDate,
                    displayStatus,
                    VendorOrderStatusUtil.statusKey(displayStatus),
                    vendorTotal,
                    totalQty,
                    items.size(),
                    items
            ));
        } catch (SQLException e) {
            System.err.println("vendor order detail: " + e.getMessage());
            return Optional.empty();
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public boolean updateStatusForVendor(int vendorUserId, int orderDbId, String dbStatus) {
        if (orderDbId <= 0 || dbStatus == null || dbStatus.isBlank()) {
            return false;
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (!vendorHasOrder(conn, vendorUserId, orderDbId)) {
                return false;
            }
            String sql = "UPDATE orders SET status = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, dbStatus);
                ps.setInt(2, orderDbId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("vendor order status update: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private static List<VendorOrderLineItem> loadLineItems(Connection conn, int orderDbId, int vendorUserId)
            throws SQLException {
        List<VendorOrderLineItem> items = new ArrayList<>();
        String sql = """
                SELECT p.name, p.unit, oi.quantity, oi.price_at_purchase
                FROM order_items oi
                INNER JOIN products p ON p.id = oi.product_id
                WHERE oi.order_id = ? AND p.vendor_user_id = ?
                ORDER BY p.name ASC
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderDbId);
            ps.setInt(2, vendorUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new VendorOrderLineItem(
                            rs.getString("name"),
                            rs.getString("unit"),
                            rs.getInt("quantity"),
                            rs.getDouble("price_at_purchase")
                    ));
                }
            }
        }
        return items;
    }

    private static boolean vendorHasOrder(Connection conn, int vendorUserId, int orderDbId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(VENDOR_ORDER_EXISTS)) {
            ps.setInt(1, orderDbId);
            ps.setInt(2, vendorUserId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    static VendorOrderRow mapOrderRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String displayStatus = VendorOrderStatusUtil.toDisplayStatus(rs.getString("status"));
        Timestamp created = rs.getTimestamp("created_at");
        String orderDate = created != null
                ? created.toLocalDateTime().toLocalDate().format(ORDER_DATE_FMT)
                : "";
        return new VendorOrderRow(
                id,
                VendorOrderStatusUtil.formatOrderId(id),
                rs.getString("customer_name"),
                rs.getString("phone"),
                orderDate,
                rs.getDouble("vendor_total"),
                displayStatus
        );
    }
}
