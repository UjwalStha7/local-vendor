package com.learninglog.dao;

import com.learninglog.entity.Product;
import com.learninglog.model.ProductBrowseParams;
import com.learninglog.model.VendorOption;
import com.learninglog.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    private static final String BASE_FROM = """
            FROM products p
            JOIN users u ON u.id = p.vendor_user_id
            LEFT JOIN vendor_profiles vp ON vp.vendor_user_id = u.id
            """;

    private static final String SELECT_ROW = """
            SELECT p.id, p.vendor_user_id, p.name, p.category, p.description, p.price, p.unit,
                   p.stock_quantity, p.photo_path, p.is_active,
                   COALESCE(NULLIF(TRIM(vp.business_name), ''), u.username) AS vendor_name
            """;

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setVendorUserId(rs.getInt("vendor_user_id"));
        p.setName(rs.getString("name"));
        p.setCategory(rs.getString("category"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price").doubleValue());
        p.setUnit(rs.getString("unit"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
        p.setPhotoPath(rs.getString("photo_path"));
        p.setActive(rs.getInt("is_active") == 1);
        p.setVendorDisplayName(rs.getString("vendor_name"));
        return p;
    }

    @Override
    public Product findById(int id) throws SQLException {
        String sql = SELECT_ROW + BASE_FROM + " WHERE p.id = ? AND p.is_active = 1";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public List<Product> findFeatured(int limit) throws SQLException {
        ProductBrowseParams p = new ProductBrowseParams();
        p.setPage(1);
        p.setPageSize(Math.max(1, limit));
        p.setSort("featured");
        return browse(p);
    }

    @Override
    public List<Product> browse(ProductBrowseParams params) throws SQLException {
        String sql = SELECT_ROW + BASE_FROM + buildWhereClause(params) + " " + orderClause(params.getSort())
                + " LIMIT ? OFFSET ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            int i = bindWhere(params, ps, 1);
            ps.setInt(i++, params.getPageSize());
            ps.setInt(i, params.getOffset());
            ResultSet rs = ps.executeQuery();
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public int countBrowse(ProductBrowseParams params) throws SQLException {
        String sql = "SELECT COUNT(*) " + BASE_FROM + buildWhereClause(params);
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            bindWhere(params, ps, 1);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public int countSellableProducts() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products p WHERE p.is_active = 1 AND p.stock_quantity > 0";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public List<VendorOption> listVendorsWithProducts() throws SQLException {
        String sql = """
                SELECT DISTINCT p.vendor_user_id,
                       COALESCE(NULLIF(TRIM(vp.business_name), ''), u.username) AS vendor_name
                FROM products p
                JOIN users u ON u.id = p.vendor_user_id
                LEFT JOIN vendor_profiles vp ON vp.vendor_user_id = u.id
                WHERE p.is_active = 1 AND p.stock_quantity > 0
                ORDER BY vendor_name
                """;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<VendorOption> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new VendorOption(rs.getInt("vendor_user_id"), rs.getString("vendor_name")));
            }
            return list;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private static String buildWhereClause(ProductBrowseParams params) {
        StringBuilder w = new StringBuilder(" WHERE p.is_active = 1 AND p.stock_quantity > 0 ");
        String cat = params.getCategory();
        if ("fruits".equals(cat)) {
            w.append(" AND LOWER(p.category) LIKE '%fruit%' AND LOWER(p.category) NOT LIKE '%vegetable%' ");
        } else if ("vegetables".equals(cat)) {
            w.append(" AND (LOWER(p.category) LIKE '%vegetable%' OR LOWER(p.category) LIKE '%veg%' ");
            w.append(" OR LOWER(p.category) LIKE '%greens%' OR LOWER(p.category) LIKE '%herb%') ");
        }

        if (!params.getSearch().isBlank()) {
            w.append(" AND (LOWER(p.name) LIKE ? OR LOWER(p.category) LIKE ? OR LOWER(COALESCE(p.description,'')) LIKE ?) ");
        }

        BigDecimal min = params.getMinPrice();
        BigDecimal max = params.getMaxPrice();
        if (min != null && min.compareTo(BigDecimal.ZERO) > 0) {
            w.append(" AND p.price >= ? ");
        }
        if (max != null && max.compareTo(BigDecimal.ZERO) > 0) {
            w.append(" AND p.price <= ? ");
        }

        List<Integer> vendors = params.getVendorUserIds();
        if (vendors != null && !vendors.isEmpty()) {
            w.append(" AND p.vendor_user_id IN (");
            for (int j = 0; j < vendors.size(); j++) {
                w.append(j == 0 ? "?" : ",?");
            }
            w.append(") ");
        }

        String avail = params.getAvailability();
        if ("in".equals(avail)) {
            w.append(" AND p.stock_quantity > 5 ");
        } else if ("limited".equals(avail)) {
            w.append(" AND p.stock_quantity > 0 AND p.stock_quantity <= 5 ");
        }
        return w.toString();
    }

    private int bindWhere(ProductBrowseParams params, PreparedStatement ps, int start) throws SQLException {
        int i = start;
        if (!params.getSearch().isBlank()) {
            String like = "%" + params.getSearch().toLowerCase() + "%";
            ps.setString(i++, like);
            ps.setString(i++, like);
            ps.setString(i++, like);
        }
        if (params.getMinPrice() != null && params.getMinPrice().compareTo(BigDecimal.ZERO) > 0) {
            ps.setBigDecimal(i++, params.getMinPrice());
        }
        if (params.getMaxPrice() != null && params.getMaxPrice().compareTo(BigDecimal.ZERO) > 0) {
            ps.setBigDecimal(i++, params.getMaxPrice());
        }
        List<Integer> vendors = params.getVendorUserIds();
        if (vendors != null) {
            for (Integer vid : vendors) {
                ps.setInt(i++, vid);
            }
        }
        return i;
    }

    private static String orderClause(String sort) {
        return switch (sort) {
            case "price-asc" -> " ORDER BY p.price ASC, p.id ASC ";
            case "price-desc" -> " ORDER BY p.price DESC, p.id DESC ";
            case "name-asc" -> " ORDER BY LOWER(p.name) ASC ";
            default -> " ORDER BY p.created_at DESC, p.id DESC ";
        };
    }
}
