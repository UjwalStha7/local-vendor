package com.learninglog.util;

import com.learninglog.entity.User;

/**
 * Email-domain rules for login routing:
 * <ul>
 *   <li>{@code *@krishak.np} (except admin) → vendor / farmer panel</li>
 *   <li>{@code krishakadmin@krishak.np} → admin panel</li>
 *   <li>Any other registered email → customer panel</li>
 * </ul>
 */
public final class LoginAuthUtil {

    public static final String ADMIN_EMAIL = "krishakadmin@krishak.np";
    public static final String ADMIN_DEFAULT_PASSWORD = "ukdijs0987654321";
    public static final String VENDOR_EMAIL_SUFFIX = "@krishak.np";

    private LoginAuthUtil() {
    }

    public static boolean isAdminBootstrapPassword(String password) {
        return password != null && ADMIN_DEFAULT_PASSWORD.equals(password);
    }

    public enum AccountType {
        ADMIN, VENDOR, CUSTOMER
    }

    public static String normalizeEmail(String email) {
        if (email == null) {
            return "";
        }
        return email.trim().toLowerCase();
    }

    public static AccountType resolveAccountType(String email) {
        String normalized = normalizeEmail(email);
        if (ADMIN_EMAIL.equals(normalized)) {
            return AccountType.ADMIN;
        }
        if (normalized.endsWith(VENDOR_EMAIL_SUFFIX)) {
            return AccountType.VENDOR;
        }
        return AccountType.CUSTOMER;
    }

    public static boolean roleMatchesAccountType(String role, AccountType accountType) {
        if (role == null) {
            return false;
        }
        return switch (accountType) {
            case ADMIN -> "admin".equalsIgnoreCase(role);
            case VENDOR -> "vendor".equalsIgnoreCase(role);
            case CUSTOMER -> "customer".equalsIgnoreCase(role);
        };
    }

    public static String redirectPath(AccountType accountType) {
        return switch (accountType) {
            case ADMIN -> "/admin/dashboard";
            case VENDOR -> "/farmer/dashboard";
            case CUSTOMER -> "/customer/home";
        };
    }

    public static String wrongAccountTypeMessage(AccountType expected) {
        return switch (expected) {
            case ADMIN -> "This login is for administrators only. Use krishakadmin@krishak.np.";
            case VENDOR -> "Farmer accounts must use an email ending with @krishak.np (e.g. you@krishak.np).";
            case CUSTOMER ->
                    "Customer accounts use a normal email (e.g. name@gmail.com). "
                            + "Farmers should sign in with their @krishak.np address.";
        };
    }

    /** Session role string stored after successful login. */
    public static String sessionRoleFor(AccountType accountType) {
        return switch (accountType) {
            case ADMIN -> "admin";
            case VENDOR -> "vendor";
            case CUSTOMER -> "customer";
        };
    }
}
