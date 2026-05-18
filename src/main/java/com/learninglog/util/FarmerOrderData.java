package com.learninglog.util;

import com.learninglog.model.VendorOrderRow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Demo order data for the vendor Order Management screen (matches UI mockup). */
public final class FarmerOrderData {

    private static final List<VendorOrderRow> SAMPLE = List.of(
            new VendorOrderRow("ORD-001", "John Doe", "555-0101", "2026-05-15", 24.95, "Pending"),
            new VendorOrderRow("ORD-002", "Jane Smith", "555-0102", "2026-05-16", 18.97, "Dispatched"),
            new VendorOrderRow("ORD-003", "Mike Johnson", "555-0103", "2026-05-17", 31.95, "Dispatched"),
            new VendorOrderRow("ORD-004", "Sarah Williams", "555-0104", "2026-05-17", 15.97, "Pending"),
            new VendorOrderRow("ORD-005", "David Brown", "555-0105", "2026-05-18", 22.96, "Delivered"),
            new VendorOrderRow("ORD-006", "Emily Davis", "555-0106", "2026-05-18", 29.94, "Pending"),
            new VendorOrderRow("ORD-007", "Robert Miller", "555-0107", "2026-05-14", 35.92, "Delivered"),
            new VendorOrderRow("ORD-008", "Lisa Anderson", "555-0108", "2026-05-13", 41.92, "Dispatched")
    );

    private FarmerOrderData() {
    }

    public static List<VendorOrderRow> allOrders() {
        List<VendorOrderRow> copy = new ArrayList<>();
        for (VendorOrderRow row : SAMPLE) {
            copy.add(new VendorOrderRow(
                    row.getOrderId(),
                    row.getCustomerName(),
                    row.getPhone(),
                    row.getOrderDate(),
                    row.getTotal(),
                    row.getStatus()
            ));
        }
        return copy;
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
                default -> { /* ignore */ }
            }
        }
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("total", orders.size());
        counts.put("pending", pending);
        counts.put("dispatched", dispatched);
        counts.put("delivered", delivered);
        return counts;
    }

    public static List<VendorOrderRow> filter(List<VendorOrderRow> source, String statusFilter, String search) {
        String status = statusFilter == null ? "all" : statusFilter.trim().toLowerCase(Locale.ROOT);
        String q = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);

        List<VendorOrderRow> out = new ArrayList<>();
        for (VendorOrderRow row : source) {
            if (!"all".equals(status) && !row.getStatusKey().equals(status)) {
                continue;
            }
            if (!q.isEmpty()) {
                String hay = (row.getOrderId() + " " + row.getCustomerName() + " " + row.getPhone()).toLowerCase(Locale.ROOT);
                if (!hay.contains(q)) {
                    continue;
                }
            }
            out.add(row);
        }
        return out;
    }

    public static String normalizeStatus(String raw) {
        if (raw == null) {
            return "Pending";
        }
        return switch (raw.trim().toLowerCase(Locale.ROOT)) {
            case "dispatched" -> "Dispatched";
            case "delivered" -> "Delivered";
            default -> "Pending";
        };
    }
}
