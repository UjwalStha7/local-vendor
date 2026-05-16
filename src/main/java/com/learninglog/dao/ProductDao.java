package com.learninglog.dao;

import com.learninglog.entity.Product;
import com.learninglog.model.ProductBrowseParams;
import com.learninglog.model.VendorOption;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {

    Product findById(int id) throws SQLException;

    List<Product> findFeatured(int limit) throws SQLException;

    List<Product> browse(ProductBrowseParams params) throws SQLException;

    int countBrowse(ProductBrowseParams params) throws SQLException;

    /** Active products with stock &gt; 0 (what the storefront can show). */
    int countSellableProducts() throws SQLException;

    List<VendorOption> listVendorsWithProducts() throws SQLException;
}
