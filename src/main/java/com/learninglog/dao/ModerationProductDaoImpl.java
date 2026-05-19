package com.learninglog.dao;

import com.learninglog.model.ModerationProduct;
import com.learninglog.model.ProductRow;
import com.learninglog.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ModerationProductDaoImpl implements ModerationProductDao {

    private static final DateTimeFormatter SUBMITTED_FMT =
            DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS product_moderation_requests (
                id INT AUTO_INCREMENT PRIMARY KEY,
                product_id INT NOT NULL,
                vendor_user_id INT NOT NULL,
                status ENUM('pending', 'approved', 'rejected') NOT NULL DEFAULT 'pending',
                prev_name VARCHAR(100) NOT NULL,
                prev_category VARCHAR(50) NOT NULL,
                prev_description TEXT,
                prev_price DECIMAL(10, 2) NOT NULL,
                prev_unit VARCHAR(20) NOT NULL,
                prev_stock_quantity INT NOT NULL,
                prev_photo_path VARCHAR(255),
                proposed_name VARCHAR(100) NOT NULL,
                proposed_category VARCHAR(50) NOT NULL,
                proposed_description TEXT,
                proposed_price DECIMAL(10, 2) NOT NULL,
                proposed_unit VARCHAR(20) NOT NULL,
                proposed_stock_quantity INT NOT NULL,
                proposed_photo_path VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                reviewed_at TIMESTAMP NULL,
                FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
                FOREIGN KEY (vendor_user_id) REFERENCES users(id) ON DELETE CASCADE
            )
            """;

    private static volatile boolean tableEnsured;

    @Override
    public List<ModerationProduct> listByFilter(String filter) {
        ensureTable();
        if (filter == null || filter.isBlank()) {
            filter = "pending";
        }
        filter = filter.toLowerCase(Locale.ROOT).trim();

        String sql = """
                SELECT r.id, r.product_id, r.status, r.created_at,
                       r.prev_name, r.prev_category, r.prev_description, r.prev_price, r.prev_unit,
                       r.prev_stock_quantity, r.prev_photo_path,
                       r.proposed_name, r.proposed_category, r.proposed_description, r.proposed_price,
                       r.proposed_unit, r.proposed_stock_quantity, r.proposed_photo_path,
                       COALESCE(vp.business_name, u.username) AS vendor_name
                FROM product_moderation_requests r
                INNER JOIN users u ON u.id = r.vendor_user_id
                LEFT JOIN vendor_profiles vp ON vp.vendor_user_id = u.id
                """;
        if (!"all".equals(filter)) {
            sql += " WHERE r.status = ?";
        }
        sql += " ORDER BY r.created_at DESC";

        List<ModerationProduct> out = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (!"all".equals(filter)) {
                    ps.setString(1, filter);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        out.add(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("product_moderation list: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return out;
    }

    @Override
    public void submitChangeRequest(int vendorUserId, ProductRow current, String proposedName, String proposedCategory,
                                   String proposedDescription, double proposedPrice, String proposedUnit,
                                   int proposedStock, String proposedPhotoPath) {
        ensureTable();
        if (new ProductDaoImpl().hasPendingModeration(current.getId())) {
            throw new IllegalStateException("A pending change request already exists for this product.");
        }
        String photo = proposedPhotoPath != null && !proposedPhotoPath.isBlank()
                ? proposedPhotoPath
                : current.getPhotoPath();

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    INSERT INTO product_moderation_requests
                    (product_id, vendor_user_id, status,
                     prev_name, prev_category, prev_description, prev_price, prev_unit, prev_stock_quantity, prev_photo_path,
                     proposed_name, proposed_category, proposed_description, proposed_price, proposed_unit,
                     proposed_stock_quantity, proposed_photo_path)
                    VALUES (?, ?, 'pending',
                            ?, ?, ?, ?, ?, ?, ?,
                            ?, ?, ?, ?, ?, ?, ?)
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, current.getId());
                ps.setInt(2, vendorUserId);
                ps.setString(3, current.getName());
                ps.setString(4, current.getCategory());
                ps.setString(5, blankToNull(current.getDescription()));
                ps.setDouble(6, current.getPrice());
                ps.setString(7, current.getUnit());
                ps.setInt(8, current.getStock());
                ps.setString(9, current.getPhotoPath());
                ps.setString(10, proposedName);
                ps.setString(11, proposedCategory);
                ps.setString(12, blankToNull(proposedDescription));
                ps.setDouble(13, proposedPrice);
                ps.setString(14, proposedUnit);
                ps.setInt(15, proposedStock);
                ps.setString(16, photo);
                ps.executeUpdate();
            }
            String flagSql = "UPDATE products SET is_flagged = 1 WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(flagSql)) {
                ps.setInt(1, current.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("product_moderation submit: " + e.getMessage());
            throw new IllegalStateException("Could not submit product change for moderation.");
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public void approve(int requestId) {
        ensureTable();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String select = """
                    SELECT product_id, proposed_name, proposed_category, proposed_description,
                           proposed_price, proposed_unit, proposed_stock_quantity, proposed_photo_path
                    FROM product_moderation_requests WHERE id = ? AND status = 'pending'
                    """;
            int productId;
            String name;
            String category;
            String description;
            double price;
            String unit;
            int stock;
            String photo;

            try (PreparedStatement ps = conn.prepareStatement(select)) {
                ps.setInt(1, requestId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return;
                    }
                    productId = rs.getInt("product_id");
                    name = rs.getString("proposed_name");
                    category = rs.getString("proposed_category");
                    description = rs.getString("proposed_description");
                    price = rs.getDouble("proposed_price");
                    unit = rs.getString("proposed_unit");
                    stock = rs.getInt("proposed_stock_quantity");
                    photo = rs.getString("proposed_photo_path");
                }
            }

            String updateProduct = """
                    UPDATE products SET name = ?, category = ?, description = ?, price = ?, unit = ?,
                           stock_quantity = ?, photo_path = ?, is_flagged = 0
                    WHERE id = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(updateProduct)) {
                ps.setString(1, name);
                ps.setString(2, category);
                ps.setString(3, description);
                ps.setDouble(4, price);
                ps.setString(5, unit);
                ps.setInt(6, stock);
                ps.setString(7, photo);
                ps.setInt(8, productId);
                ps.executeUpdate();
            }

            String updateRequest = """
                    UPDATE product_moderation_requests SET status = 'approved', reviewed_at = ?
                    WHERE id = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(updateRequest)) {
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                ps.setInt(2, requestId);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            System.err.println("product_moderation approve: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
                DatabaseConnection.closeConnection(conn);
            }
        }
    }

    @Override
    public void reject(int requestId) {
        ensureTable();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String select = "SELECT product_id FROM product_moderation_requests WHERE id = ? AND status = 'pending'";
            int productId;
            try (PreparedStatement ps = conn.prepareStatement(select)) {
                ps.setInt(1, requestId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return;
                    }
                    productId = rs.getInt("product_id");
                }
            }

            String updateRequest = """
                    UPDATE product_moderation_requests SET status = 'rejected', reviewed_at = ?
                    WHERE id = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(updateRequest)) {
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                ps.setInt(2, requestId);
                ps.executeUpdate();
            }

            String unflag = "UPDATE products SET is_flagged = 0 WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(unflag)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            System.err.println("product_moderation reject: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
                DatabaseConnection.closeConnection(conn);
            }
        }
    }

    public static void ensureSchema() {
        new ModerationProductDaoImpl().ensureTable();
    }

    private void ensureTable() {
        if (tableEnsured) {
            return;
        }
        synchronized (ModerationProductDaoImpl.class) {
            if (tableEnsured) {
                return;
            }
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                try (PreparedStatement ps = conn.prepareStatement(CREATE_TABLE)) {
                    ps.execute();
                }
                tableEnsured = true;
            } catch (SQLException e) {
                System.err.println("product_moderation ensure table: " + e.getMessage());
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }

    private static ModerationProduct mapRow(ResultSet rs) throws SQLException {
        Timestamp created = rs.getTimestamp("created_at");
        String submitted = created != null
                ? created.toLocalDateTime().toLocalDate().format(SUBMITTED_FMT)
                : LocalDate.now().format(SUBMITTED_FMT);
        return new ModerationProduct(
                rs.getInt("id"),
                rs.getInt("product_id"),
                rs.getString("vendor_name"),
                submitted,
                rs.getString("status"),
                rs.getString("prev_name"),
                rs.getString("prev_category"),
                rs.getString("prev_description"),
                rs.getDouble("prev_price"),
                rs.getString("prev_unit"),
                rs.getInt("prev_stock_quantity"),
                rs.getString("prev_photo_path"),
                rs.getString("proposed_name"),
                rs.getString("proposed_category"),
                rs.getString("proposed_description"),
                rs.getDouble("proposed_price"),
                rs.getString("proposed_unit"),
                rs.getInt("proposed_stock_quantity"),
                rs.getString("proposed_photo_path")
        );
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
