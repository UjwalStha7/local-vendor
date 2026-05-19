package com.learninglog.dao;

import com.learninglog.model.VendorAccountCard;
import com.learninglog.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VendorAccountDaoImpl implements VendorAccountDao {

    private final VendorRequestDao vendorRequestDao = new VendorRequestDaoImpl();
    private final VendorProfileDao profileDao = new VendorProfileDaoImpl();

    public VendorAccountDaoImpl() {
        profileDao.ensureSchema();
    }

    private static final String LIST_SQL = """
            SELECT u.id, u.username, u.email, u.phone, u.is_active,
                   COALESCE(vp.business_name, u.username) AS business_name,
                   COALESCE(vp.shop_bio, '') AS shop_bio,
                   COALESCE(vp.address, '') AS address,
                   COALESCE(vp.logo_path, '') AS logo_path,
                   (SELECT COUNT(*) FROM products p WHERE p.vendor_user_id = u.id) AS product_count,
                   (SELECT COUNT(DISTINCT oi.order_id)
                    FROM order_items oi
                    INNER JOIN products p ON p.id = oi.product_id
                    WHERE p.vendor_user_id = u.id) AS order_count
            FROM users u
            LEFT JOIN vendor_profiles vp ON vp.vendor_user_id = u.id
            WHERE LOWER(u.role) = 'vendor'
            ORDER BY u.username ASC
            """;

    private static final String SEARCH_SQL = """
            SELECT u.id, u.username, u.email, u.phone, u.is_active,
                   COALESCE(vp.business_name, u.username) AS business_name,
                   COALESCE(vp.shop_bio, '') AS shop_bio,
                   COALESCE(vp.address, '') AS address,
                   COALESCE(vp.logo_path, '') AS logo_path,
                   (SELECT COUNT(*) FROM products p WHERE p.vendor_user_id = u.id) AS product_count,
                   (SELECT COUNT(DISTINCT oi.order_id)
                    FROM order_items oi
                    INNER JOIN products p ON p.id = oi.product_id
                    WHERE p.vendor_user_id = u.id) AS order_count
            FROM users u
            LEFT JOIN vendor_profiles vp ON vp.vendor_user_id = u.id
            WHERE LOWER(u.role) = 'vendor'
              AND (
                    LOWER(u.username) LIKE ?
                 OR LOWER(u.email) LIKE ?
                 OR LOWER(COALESCE(vp.business_name, '')) LIKE ?
                 OR LOWER(COALESCE(u.phone, '')) LIKE ?
              )
            ORDER BY u.username ASC
            """;

    @Override
    public List<VendorAccountCard> search(String query) {
        if (query == null || query.isBlank()) {
            return listAll();
        }
        String pattern = "%" + query.toLowerCase(Locale.ROOT).trim() + "%";
        List<VendorAccountCard> cards = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(SEARCH_SQL)) {
                ps.setString(1, pattern);
                ps.setString(2, pattern);
                ps.setString(3, pattern);
                ps.setString(4, pattern);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        cards.add(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor accounts search: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return cards;
    }

    @Override
    public boolean deleteVendor(int vendorUserId) {
        if (vendorUserId <= 0) {
            return false;
        }
        vendorRequestDao.markRequestsRejectedForDeletedVendor(vendorUserId);
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM users WHERE id = ? AND LOWER(role) = 'vendor'";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                return ps.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            System.err.println("vendor account delete: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private List<VendorAccountCard> listAll() {
        List<VendorAccountCard> cards = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(LIST_SQL);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cards.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor accounts list: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return cards;
    }

    private static VendorAccountCard mapRow(ResultSet rs) throws SQLException {
        return new VendorAccountCard(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("business_name"),
                rs.getString("email"),
                rs.getString("phone") != null ? rs.getString("phone") : "",
                rs.getString("shop_bio"),
                rs.getString("address"),
                rs.getString("logo_path"),
                rs.getInt("product_count"),
                rs.getInt("order_count"),
                rs.getBoolean("is_active")
        );
    }
}
