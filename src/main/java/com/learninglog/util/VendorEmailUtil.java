package com.learninglog.util;

import com.learninglog.dao.UserDao;
import com.learninglog.entity.User;

/**
 * Builds unique {@code *@krishak.np} login emails for approved farmers.
 */
public final class VendorEmailUtil {

    private VendorEmailUtil() {
    }

    public static String deriveVendorLoginEmail(String contactEmail, UserDao userDao) {
        String local = localPart(contactEmail);
        String base = sanitizeLocalPart(local);
        if (base.isBlank()) {
            base = "farmer";
        }

        String candidate = base + LoginAuthUtil.VENDOR_EMAIL_SUFFIX;
        if (userDao.findByEmail(candidate) == null) {
            return candidate;
        }

        for (int i = 2; i < 1000; i++) {
            candidate = base + i + LoginAuthUtil.VENDOR_EMAIL_SUFFIX;
            if (userDao.findByEmail(candidate) == null) {
                return candidate;
            }
        }
        return base + System.currentTimeMillis() + LoginAuthUtil.VENDOR_EMAIL_SUFFIX;
    }

    private static String localPart(String email) {
        if (email == null) {
            return "";
        }
        String normalized = email.trim().toLowerCase();
        int at = normalized.indexOf('@');
        return at > 0 ? normalized.substring(0, at) : normalized;
    }

    private static String sanitizeLocalPart(String local) {
        return local.replaceAll("[^a-z0-9._-]", "").replaceAll("^\\.+|\\.+$", "");
    }
}
