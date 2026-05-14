package com.learninglog.dao;

import com.learninglog.model.VendorAccountCard;

import java.util.List;

public interface VendorAccountDao {

    /**
     * @param query trimmed search text; empty returns all vendors
     */
    List<VendorAccountCard> search(String query);
}
