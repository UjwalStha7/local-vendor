package com.learninglog.dao;

import com.learninglog.model.CatalogProduct;
import com.learninglog.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class CatalogDaoImpl implements CatalogDao {

    private static final String BASE_FROM = """
            FROM products p
            INNER JOIN users u ON u.id = p.vendor_user_id AND LOWER(u.role) = 'vendor'
            LEFT JOIN vendor_profiles vp ON vp.vendor_user_id = p.vendor_user_id
            WHERE (p.is_active = 1 OR p.is_active IS NULL)
              AND (p.is_flagged = 0 OR p.is_flagged IS NULL)
              AND NOT EXISTS (
                  SELECT 1 FROM product_moderation_requests r
                  WHERE r.product_id = p.id AND r.status = 'pending'
              )
            """;

    @Override
    public Optional<CatalogProduct> findById(int productId) {
        if (productId <= 0) {
            return Optional.empty();
        }
        String sql = """
                SELECT p.id, p.name, p.category, p.description, p.price, p.unit, p.stock_quantity, p.photo_path,
                       COALESCE(NULLIF(TRIM(vp.business_name), ''), u.username) AS vendor_name
                """ + BASE_FROM + " AND p.id = ? ";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("catalog findById: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<CatalogProduct> search(String query, List<String> categories, List<String> vendorSlugs,
                                       Double minPrice, Double maxPrice, String availability, String sortBy,
                                       int limit, boolean inStockOnly) {
        List<CatalogProduct> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT p.id, p.name, p.category, p.description, p.price, p.unit, p.stock_quantity, p.photo_path,
                       COALESCE(NULLIF(TRIM(vp.business_name), ''), u.username) AS vendor_name
                """);
        sql.append(BASE_FROM);

        List<Object> params = new ArrayList<>();

        if (query != null && !query.isBlank()) {
            sql.append("""
                     AND (
                         LOWER(p.name) LIKE ?
                      OR LOWER(p.category) LIKE ?
                      OR LOWER(COALESCE(vp.business_name, u.username)) LIKE ?
                      OR LOWER(COALESCE(p.description, '')) LIKE ?
                     )
                    """);
            String pattern = "%" + query.trim().toLowerCase(Locale.ROOT) + "%";
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }

        if (categories != null && !categories.isEmpty()) {
            sql.append(" AND p.category IN (");
            sql.append(String.join(",", java.util.Collections.nCopies(categories.size(), "?")));
            sql.append(") ");
            categories.forEach(params::add);
        }

        if (vendorSlugs != null && !vendorSlugs.isEmpty()) {
            sql.append(" AND LOWER(REPLACE(COALESCE(vp.business_name, u.username), ' ', '-')) IN (");
            sql.append(String.join(",", java.util.Collections.nCopies(vendorSlugs.size(), "?")));
            sql.append(") ");
            for (String slug : vendorSlugs) {
                params.add(slug.trim().toLowerCase(Locale.ROOT));
            }
        }

        if (minPrice != null) {
            sql.append(" AND p.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND p.price <= ? ");
            params.add(maxPrice);
        }

        String avail = availability == null ? "all" : availability.trim().toLowerCase(Locale.ROOT);
        if (inStockOnly) {
            sql.append(" AND p.stock_quantity > 0 ");
        } else if ("in".equals(avail)) {
            sql.append(" AND p.stock_quantity > 5 ");
        } else if ("limited".equals(avail)) {
            sql.append(" AND p.stock_quantity > 0 AND p.stock_quantity <= 5 ");
        }

        sql.append(" ORDER BY ").append(orderClause(sortBy));

        if (limit > 0) {
            sql.append(" LIMIT ? ");
            params.add(limit);
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        products.add(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("catalog search: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return products;
    }

    @Override
    public List<String> listDistinctCategories() {
        List<String> categories = new ArrayList<>();
        String sql = """
                SELECT DISTINCT p.category
                FROM products p
                INNER JOIN users u ON u.id = p.vendor_user_id AND LOWER(u.role) = 'vendor'
                WHERE (p.is_active = 1 OR p.is_active IS NULL)
                  AND (p.is_flagged = 0 OR p.is_flagged IS NULL)
                  AND NOT EXISTS (
                      SELECT 1 FROM product_moderation_requests r
                      WHERE r.product_id = p.id AND r.status = 'pending'
                  )
                ORDER BY p.category ASC
                """;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String cat = rs.getString("category");
                    if (cat != null && !cat.isBlank()) {
                        categories.add(cat.trim());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("catalog categories: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return categories;
    }

    @Override
    public List<Map<String, String>> listVendors() {
        Map<String, Map<String, String>> bySlug = new LinkedHashMap<>();
        String sql = """
                SELECT DISTINCT
                       COALESCE(NULLIF(TRIM(vp.business_name), ''), u.username) AS vendor_name
                FROM products p
                INNER JOIN users u ON u.id = p.vendor_user_id AND LOWER(u.role) = 'vendor'
                LEFT JOIN vendor_profiles vp ON vp.vendor_user_id = p.vendor_user_id
                WHERE (p.is_active = 1 OR p.is_active IS NULL)
                  AND (p.is_flagged = 0 OR p.is_flagged IS NULL)
                  AND NOT EXISTS (
                      SELECT 1 FROM product_moderation_requests r
                      WHERE r.product_id = p.id AND r.status = 'pending'
                  )
                ORDER BY vendor_name ASC
                """;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("vendor_name");
                    if (name == null || name.isBlank()) {
                        continue;
                    }
                    String slug = CatalogProduct.toVendorSlug(name);
                    Map<String, String> entry = new LinkedHashMap<>();
                    entry.put("name", name.trim());
                    entry.put("slug", slug);
                    bySlug.putIfAbsent(slug, entry);
                }
            }
        } catch (SQLException e) {
            System.err.println("catalog vendors: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return new ArrayList<>(bySlug.values());
    }

    private static CatalogProduct mapRow(ResultSet rs) throws SQLException {
        String vendorName = rs.getString("vendor_name");
        return new CatalogProduct(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("description"),
                vendorName,
                CatalogProduct.toVendorSlug(vendorName),
                rs.getDouble("price"),
                rs.getString("unit"),
                rs.getInt("stock_quantity"),
                rs.getString("photo_path")
        );
    }

    private static String orderClause(String sortBy) {
        if (sortBy == null) {
            return "p.name ASC";
        }
        return switch (sortBy.trim().toLowerCase(Locale.ROOT)) {
            case "price-asc" -> "p.price ASC, p.name ASC";
            case "price-desc" -> "p.price DESC, p.name ASC";
            case "name-asc" -> "p.name ASC";
            default -> "p.id DESC";
        };
    }
}
