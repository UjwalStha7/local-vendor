package com.learninglog.dao;

import com.learninglog.model.ProductRow;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    List<ProductRow> listByVendor(int vendorUserId);

    Optional<ProductRow> findByIdForVendor(int productId, int vendorUserId);

    boolean hasPendingModeration(int productId);
}
