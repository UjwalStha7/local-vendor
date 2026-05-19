package com.learninglog.dao;

import com.learninglog.model.VendorAccountCard;

import java.util.List;

public interface VendorAccountDao {

    /**
     * @param query trimmed search text; empty returns all vendors
     */
    List<VendorAccountCard> search(String query);

    /** Deletes a vendor user account. Returns false if not found or not a vendor. */
    boolean deleteVendor(int vendorUserId);
}
