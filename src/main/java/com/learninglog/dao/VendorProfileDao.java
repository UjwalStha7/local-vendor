package com.learninglog.dao;

import com.learninglog.entity.User;
import com.learninglog.model.VendorProfile;

import java.util.Optional;

public interface VendorProfileDao {

    void ensureSchema();

    VendorProfile loadForVendor(User vendor);

    boolean saveProfile(int vendorUserId, String shopName, String shopBio, String phone,
                        String address, String logoPath);

    Optional<String> findBusinessName(int vendorUserId);

    Optional<String> findLogoPath(int vendorUserId);
}
