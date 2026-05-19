package com.learninglog.util;

import java.util.List;
import java.util.Locale;

public final class ProductCategoryUtil {

    public static final List<String> CATEGORIES = List.of(
            "Fruits",
            "Vegetables",
            "Grains",
            "Dairy",
            "Other"
    );

    private ProductCategoryUtil() {
    }

    public static boolean isValid(String category) {
        if (category == null || category.isBlank()) {
            return false;
        }
        String normalized = category.trim();
        for (String allowed : CATEGORIES) {
            if (allowed.equalsIgnoreCase(normalized)) {
                return true;
            }
        }
        return false;
    }

    /** Returns canonical label (e.g. "fruits" → "Fruits") or null if invalid. */
    public static String normalize(String category) {
        if (category == null || category.isBlank()) {
            return null;
        }
        String normalized = category.trim();
        for (String allowed : CATEGORIES) {
            if (allowed.equalsIgnoreCase(normalized)) {
                return allowed;
            }
        }
        return null;
    }
}
