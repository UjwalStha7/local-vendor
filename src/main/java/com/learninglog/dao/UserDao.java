package com.learninglog.dao;

import com.learninglog.entity.User;

public interface UserDao {
    boolean insertUser(User user);

    User findByEmail(String email);

    User findByUsername(String username);

    User findById(int id);

    boolean updateRole(int userId, String role);

    boolean hasVendorProfile(int userId);
}
