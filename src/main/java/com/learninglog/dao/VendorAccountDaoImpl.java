package com.learninglog.dao;

import com.learninglog.model.VendorAccountCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class VendorAccountDaoImpl implements VendorAccountDao {

    private static final List<VendorAccountCard> ALL = Arrays.asList(
            new VendorAccountCard(
                    "Amit Patel",
                    "Green Farms Co.",
                    "amit@greenfarms.com",
                    "+91 99887 76655",
                    45,
                    234,
                    true
            ),
            new VendorAccountCard(
                    "Sunita Verma",
                    "Fresh Produce Ltd",
                    "sunita@freshproduce.com",
                    "+91 97654 32109",
                    32,
                    189,
                    true
            ),
            new VendorAccountCard(
                    "Rahul Singh",
                    "Organic Valley",
                    "rahul@organicvalley.com",
                    "+91 98123 45670",
                    28,
                    156,
                    true
            )
    );

    @Override
    public List<VendorAccountCard> search(String query) {
        if (query == null || query.isBlank()) {
            return new ArrayList<>(ALL);
        }
        String q = query.toLowerCase(Locale.ROOT).trim();
        List<VendorAccountCard> out = new ArrayList<>();
        for (VendorAccountCard v : ALL) {
            if (v.getName().toLowerCase(Locale.ROOT).contains(q)
                    || v.getCompany().toLowerCase(Locale.ROOT).contains(q)
                    || v.getEmail().toLowerCase(Locale.ROOT).contains(q)
                    || v.getPhone().toLowerCase(Locale.ROOT).contains(q)) {
                out.add(v);
            }
        }
        return out;
    }
}
