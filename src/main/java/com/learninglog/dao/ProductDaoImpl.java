package com.learninglog.dao;

import com.learninglog.model.ProductRow;
import com.learninglog.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao {

    private static final String LIST_SQL = """
            SELECT p.id, p.name, p.category, p.description, p.price, p.unit,
                   p.stock_quantity, p.photo_path,
                   EXISTS (
                       SELECT 1 FROM product_moderation_requests r
                       WHERE r.product_id = p.id AND r.status = 'pending'
                   ) AS pending_mod
            FROM products p
            WHERE p.vendor_user_id = ?
            ORDER BY p.name ASC
            """;

    private static final String FIND_SQL = """
            SELECT p.id, p.name, p.category, p.description, p.price, p.unit,
                   p.stock_quantity, p.photo_path,
                   EXISTS (
                       SELECT 1 FROM product_moderation_requests r
                       WHERE r.product_id = p.id AND r.status = 'pending'
                   ) AS pending_mod
            FROM products p
            WHERE p.id = ? AND p.vendor_user_id = ?
            """;

    @Override
    public List<ProductRow> listByVendor(int vendorUserId) {
        ModerationProductDaoImpl.ensureSchema();
        List<ProductRow> rows = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(LIST_SQL)) {
                ps.setInt(1, vendorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rows.add(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("products listByVendor: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return rows;
    }

    @Override
    public Optional<ProductRow> findByIdForVendor(int productId, int vendorUserId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(FIND_SQL)) {
                ps.setInt(1, productId);
                ps.setInt(2, vendorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("products findByIdForVendor: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public boolean hasPendingModeration(int productId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT 1 FROM product_moderation_requests
                    WHERE product_id = ? AND status = 'pending' LIMIT 1
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private static ProductRow mapRow(ResultSet rs) throws SQLException {
        String photo = rs.getString("photo_path");
        if (photo == null || photo.isBlank()) {
            photo = null;
        }
        return new ProductRow(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getString("unit"),
                rs.getInt("stock_quantity"),
                photo,
                rs.getBoolean("pending_mod")
        );
    }
}
