package com.learninglog.util;

import com.learninglog.model.VendorOrderRow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Filtering and status helpers for vendor Order Management. */
public final class FarmerOrderData {

    private FarmerOrderData() {
    }

    public static Map<String, Integer> countByStatus(List<VendorOrderRow> orders) {
        int pending = 0;
        int dispatched = 0;
        int delivered = 0;
        for (VendorOrderRow row : orders) {
            switch (row.getStatusKey()) {
                case "pending" -> pending++;
                case "dispatched" -> dispatched++;
                case "delivered" -> delivered++;
                default -> { /* ignore cancelled etc. */ }
            }
        }
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("total", orders.size());
        counts.put("pending", pending);
        counts.put("dispatched", dispatched);
        counts.put("delivered", delivered);
        return counts;
    }

    public static List<VendorOrderRow> filterByStatus(List<VendorOrderRow> source, String statusFilter) {
        String status = statusFilter == null ? "all" : statusFilter.trim().toLowerCase(Locale.ROOT);

        List<VendorOrderRow> out = new ArrayList<>();
        for (VendorOrderRow row : source) {
            if (!"all".equals(status) && !row.getStatusKey().equals(status)) {
                continue;
            }
            out.add(row);
        }
        return out;
    }

    public static String normalizeStatus(String raw) {
        return VendorOrderStatusUtil.normalizeUiStatus(raw);
    }
}
