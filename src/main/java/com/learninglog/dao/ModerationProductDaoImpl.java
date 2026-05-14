package com.learninglog.dao;

import com.learninglog.model.ModerationProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModerationProductDaoImpl implements ModerationProductDao {

    private static final CopyOnWriteArrayList<ModerationProduct> PRODUCTS = new CopyOnWriteArrayList<>(List.of(
            new ModerationProduct(
                    1,
                    "Organic Tomatoes",
                    "Green Farms Co.",
                    "Vegetables",
                    "Rs 45/kg",
                    "2026-05-03",
                    "/image/organic_tomatoes.jpeg",
                    "pending"
            ),
            new ModerationProduct(
                    2,
                    "Fresh Spinach",
                    "Green Farms Co.",
                    "Vegetables",
                    "Rs 32/kg",
                    "2026-05-02",
                    "/image/fresh_spinach.jpg",
                    "pending"
            ),
            new ModerationProduct(
                    3,
                    "Premium Basmati Rice",
                    "Organic Valley",
                    "Grains",
                    "Rs 120/kg",
                    "2026-05-01",
                    "/image/premium_basmati_rice.jpg",
                    "approved"
            )
    ));

    @Override
    public List<ModerationProduct> listByFilter(String filter) {
        if (filter == null || filter.isBlank()) {
            filter = "all";
        }
        filter = filter.toLowerCase(Locale.ROOT).trim();
        List<ModerationProduct> out = new ArrayList<>();
        for (ModerationProduct p : PRODUCTS) {
            if ("all".equals(filter)) {
                out.add(p);
            } else if ("pending".equals(filter) && p.isPending()) {
                out.add(p);
            } else if ("approved".equals(filter) && p.isApproved()) {
                out.add(p);
            } else if ("rejected".equals(filter) && p.isRejected()) {
                out.add(p);
            }
        }
        return out;
    }

    @Override
    public void approve(int id) {
        for (ModerationProduct p : PRODUCTS) {
            if (p.getId() == id) {
                p.setStatus("approved");
                return;
            }
        }
    }

    @Override
    public void reject(int id) {
        for (ModerationProduct p : PRODUCTS) {
            if (p.getId() == id) {
                p.setStatus("rejected");
                return;
            }
        }
    }
}
