package com.learninglog.dao;

import com.learninglog.entity.User;

public interface UserDao {
    boolean insertUser(User user);
    User findByEmail(String email);
    User findByUsername(String username);


}
