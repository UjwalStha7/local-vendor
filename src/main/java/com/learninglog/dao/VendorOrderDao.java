package com.learninglog.dao;

import com.learninglog.model.VendorOrderDetail;
import com.learninglog.model.VendorOrderRow;

import java.util.List;
import java.util.Optional;

public interface VendorOrderDao {

    List<VendorOrderRow> listOrdersForVendor(int vendorUserId);

    Optional<VendorOrderDetail> findOrderDetail(int vendorUserId, int orderDbId);

    boolean updateStatusForVendor(int vendorUserId, int orderDbId, String dbStatus);
}
