package com.learninglog.dao;

import com.learninglog.entity.User;
import com.learninglog.model.VendorProfile;
import com.learninglog.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class VendorProfileDaoImpl implements VendorProfileDao {

    private static final DateTimeFormatter MEMBER_SINCE_FMT =
            DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US);

    private static volatile boolean schemaEnsured;

    @Override
    public void ensureSchema() {
        if (schemaEnsured) {
            return;
        }
        synchronized (VendorProfileDaoImpl.class) {
            if (schemaEnsured) {
                return;
            }
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                addColumnIfMissing(conn, "shop_bio", "ALTER TABLE vendor_profiles ADD COLUMN shop_bio TEXT NULL");
                addColumnIfMissing(conn, "address", "ALTER TABLE vendor_profiles ADD COLUMN address VARCHAR(255) NULL");
                addColumnIfMissing(conn, "logo_path", "ALTER TABLE vendor_profiles ADD COLUMN logo_path VARCHAR(255) NULL");
                schemaEnsured = true;
            } catch (SQLException e) {
                System.err.println("vendor_profiles schema: " + e.getMessage());
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }

    @Override
    public VendorProfile loadForVendor(User vendor) {
        ensureSchema();
        VendorProfile profile = new VendorProfile();
        if (vendor == null) {
            return profile;
        }

        profile.setEmail(nullToEmpty(vendor.getEmail()));
        profile.setPhone(nullToEmpty(vendor.getPhone()));
        profile.setVerified(vendor.Isactive());
        profile.setMemberSince(formatMemberSince(vendor.getCreatedAt()));
        profile.setResponseTime("Within 2 hours");

        String fallbackName = vendor.getUsername();
        if (fallbackName == null || fallbackName.isBlank()) {
            fallbackName = "Your shop";
        }
        profile.setShopName(fallbackName);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT business_name, shop_bio, address, logo_path
                    FROM vendor_profiles
                    WHERE vendor_user_id = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendor.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String businessName = rs.getString("business_name");
                        if (businessName != null && !businessName.isBlank()) {
                            profile.setShopName(businessName.trim());
                        }
                        profile.setShopBio(nullToEmpty(rs.getString("shop_bio")));
                        profile.setAddress(nullToEmpty(rs.getString("address")));
                        profile.setLogoUrl(nullToEmpty(rs.getString("logo_path")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor profile load: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return profile;
    }

    @Override
    public boolean saveProfile(int vendorUserId, String shopName, String shopBio, String phone,
                               String address, String logoPath) {
        ensureSchema();
        if (vendorUserId <= 0 || shopName == null || shopName.isBlank()) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET phone = ? WHERE id = ? AND LOWER(role) = 'vendor'")) {
                ps.setString(1, phone == null ? "" : phone.trim());
                ps.setInt(2, vendorUserId);
                ps.executeUpdate();
            }

            String upsert = """
                    INSERT INTO vendor_profiles (vendor_user_id, business_name, shop_bio, address, logo_path)
                    VALUES (?, ?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                        business_name = VALUES(business_name),
                        shop_bio = VALUES(shop_bio),
                        address = VALUES(address),
                        logo_path = VALUES(logo_path)
                    """;
            try (PreparedStatement ps = conn.prepareStatement(upsert)) {
                ps.setInt(1, vendorUserId);
                ps.setString(2, shopName.trim());
                ps.setString(3, shopBio == null ? "" : shopBio.trim());
                ps.setString(4, address == null ? "" : address.trim());
                ps.setString(5, logoPath == null ? "" : logoPath.trim());
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                    // ignore
                }
            }
            System.err.println("vendor profile save: " + e.getMessage());
            return false;
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

    @Override
    public Optional<String> findBusinessName(int vendorUserId) {
        ensureSchema();
        return findColumn(vendorUserId, "business_name");
    }

    @Override
    public Optional<String> findLogoPath(int vendorUserId) {
        ensureSchema();
        return findColumn(vendorUserId, "logo_path");
    }

    private Optional<String> findColumn(int vendorUserId, String column) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT " + column + " FROM vendor_profiles WHERE vendor_user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String value = rs.getString(1);
                        if (value != null && !value.isBlank()) {
                            return Optional.of(value.trim());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor profile lookup: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    private static void addColumnIfMissing(Connection conn, String column, String ddl) throws SQLException {
        String check = """
                SELECT COUNT(*) FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                  AND TABLE_NAME = 'vendor_profiles'
                  AND COLUMN_NAME = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setString(1, column);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement alter = conn.prepareStatement(ddl)) {
                        alter.executeUpdate();
                    }
                }
            }
        }
    }

    private static String formatMemberSince(Timestamp createdAt) {
        if (createdAt == null) {
            return "—";
        }
        return createdAt.toLocalDateTime().toLocalDate().format(MEMBER_SINCE_FMT);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
