package com.learninglog.dao;

import com.learninglog.util.DatabaseConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    @Override
    public int createOrder(int customerUserId, List<CheckoutLine> lines) {
        if (lines == null || lines.isEmpty()) {
            return -1;
        }

        List<ResolvedLine> resolved = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            for (CheckoutLine line : lines) {
                if (line.quantity() <= 0) {
                    conn.rollback();
                    return -1;
                }
                ResolvedLine product = loadProduct(conn, line.productId(), line.quantity());
                if (product == null) {
                    conn.rollback();
                    return -1;
                }
                resolved.add(product);
            }

            BigDecimal total = BigDecimal.ZERO;
            for (ResolvedLine line : resolved) {
                total = total.add(line.lineTotal());
            }

            int orderId = insertOrder(conn, customerUserId, total);
            if (orderId <= 0) {
                conn.rollback();
                return -1;
            }

            for (ResolvedLine line : resolved) {
                insertOrderItem(conn, orderId, line);
            }

            conn.commit();
            return orderId;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                    // ignore
                }
            }
            System.err.println("create order: " + e.getMessage());
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // ignore
                }
            }
            DatabaseConnection.closeConnection(conn);
        }
    }

    private static ResolvedLine loadProduct(Connection conn, int productId, int quantity) throws SQLException {
        String sql = """
                SELECT id, price, stock_quantity
                FROM products
                WHERE id = ? AND (is_active = 1 OR is_active IS NULL)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                int stock = rs.getInt("stock_quantity");
                if (stock < quantity) {
                    return null;
                }
                BigDecimal price = rs.getBigDecimal("price");
                if (price == null) {
                    return null;
                }
                BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(quantity))
                        .setScale(2, RoundingMode.HALF_UP);
                return new ResolvedLine(productId, quantity, price, lineTotal);
            }
        }
    }

    private static int insertOrder(Connection conn, int customerUserId, BigDecimal total) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, 'pending')";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerUserId);
            ps.setBigDecimal(2, total);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    private static void insertOrderItem(Connection conn, int orderId, ResolvedLine line) throws SQLException {
        String sql = """
                INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase)
                VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, line.productId());
            ps.setInt(3, line.quantity());
            ps.setBigDecimal(4, line.unitPrice());
            ps.executeUpdate();
        }

        String stockSql = """
                UPDATE products SET stock_quantity = stock_quantity - ?
                WHERE id = ? AND stock_quantity >= ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(stockSql)) {
            ps.setInt(1, line.quantity());
            ps.setInt(2, line.productId());
            ps.setInt(3, line.quantity());
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Insufficient stock for product " + line.productId());
            }
        }
    }

    private record ResolvedLine(int productId, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
    }
}
