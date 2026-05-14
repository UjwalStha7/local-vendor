package com.learninglog.dao;

import com.learninglog.model.ModerationProduct;

import java.util.List;

public interface ModerationProductDao {

    List<ModerationProduct> listByFilter(String filter);

    void approve(int id);

    void reject(int id);
}
