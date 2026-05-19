package com.learninglog.util;

import java.util.Locale;

/** Maps order status between DB enum and vendor portal UI. */
public final class VendorOrderStatusUtil {

    private VendorOrderStatusUtil() {
    }

    public static String toDisplayStatus(String dbStatus) {
        if (dbStatus == null) {
            return "Pending";
        }
        return switch (dbStatus.trim().toLowerCase(Locale.ROOT)) {
            case "shipped", "confirmed" -> "Dispatched";
            case "delivered" -> "Delivered";
            case "cancelled" -> "Cancelled";
            default -> "Pending";
        };
    }

    public static String toDbStatus(String uiStatus) {
        if (uiStatus == null) {
            return "pending";
        }
        return switch (uiStatus.trim().toLowerCase(Locale.ROOT)) {
            case "dispatched" -> "shipped";
            case "delivered" -> "delivered";
            default -> "pending";
        };
    }

    public static String normalizeUiStatus(String raw) {
        if (raw == null) {
            return "Pending";
        }
        return switch (raw.trim().toLowerCase(Locale.ROOT)) {
            case "dispatched" -> "Dispatched";
            case "delivered" -> "Delivered";
            default -> "Pending";
        };
    }

    public static String statusKey(String displayStatus) {
        if (displayStatus == null) {
            return "";
        }
        return displayStatus.trim().toLowerCase(Locale.ROOT);
    }

    public static String formatOrderId(int dbId) {
        return "ORD-" + String.format("%03d", dbId);
    }

    public static int parseOrderId(String orderIdParam) {
        if (orderIdParam == null || orderIdParam.isBlank()) {
            return -1;
        }
        String trimmed = orderIdParam.trim();
        if (trimmed.toUpperCase(Locale.ROOT).startsWith("ORD-")) {
            trimmed = trimmed.substring(4);
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
