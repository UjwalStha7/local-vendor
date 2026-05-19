package com.learninglog.dao;

import com.learninglog.model.CatalogProduct;

import java.util.List;
import java.util.Map;

public interface CatalogDao {

    List<CatalogProduct> search(String query, List<String> categories, List<String> vendorSlugs,
                                Double minPrice, Double maxPrice, String availability, String sortBy,
                                int limit, boolean inStockOnly);

    List<String> listDistinctCategories();

    List<Map<String, String>> listVendors();
}
