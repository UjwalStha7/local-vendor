package com.learninglog.dao;

import com.learninglog.model.ProductRow;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    List<ProductRow> listByVendor(int vendorUserId);

    List<ProductRow> searchByVendor(int vendorUserId, String query);

    Optional<ProductRow> findByIdForVendor(int productId, int vendorUserId);

    boolean hasPendingModeration(int productId);

    boolean deleteByIdForVendor(int productId, int vendorUserId);
}
