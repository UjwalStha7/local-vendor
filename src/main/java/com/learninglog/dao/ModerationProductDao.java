package com.learninglog.dao;

import com.learninglog.model.ModerationProduct;
import com.learninglog.model.ProductRow;

import java.util.List;

public interface ModerationProductDao {

    List<ModerationProduct> listByFilter(String filter);

    void submitChangeRequest(int vendorUserId, ProductRow current, String proposedName, String proposedCategory,
                               String proposedDescription, double proposedPrice, String proposedUnit,
                               int proposedStock, String proposedPhotoPath);

    void submitNewProductRequest(int vendorUserId, String name, String category, String description,
                                 double price, String unit, int stock, String photoPath);

    boolean hasPendingNewProductRequest(int vendorUserId);

    void approve(int requestId);

    void reject(int requestId);
}
