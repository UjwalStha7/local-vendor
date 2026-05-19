package com.learninglog.util;

import java.security.SecureRandom;

public final class TempPasswordUtil {

    private static final String ALPHANUM = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private TempPasswordUtil() {
    }

    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    /**
     * Temporary vendor password from the applicant contact email: the part before {@code @}.
     * Example: {@code shirshgrg@gmail.com} → {@code shirshgrg}.
     */
    public static String fromContactEmail(String contactEmail) {
        if (contactEmail == null) {
            return generate(10);
        }
        String trimmed = contactEmail.trim();
        int at = trimmed.indexOf('@');
        if (at <= 0) {
            return generate(10);
        }
        String localPart = trimmed.substring(0, at);
        if (localPart.isBlank()) {
            return generate(10);
        }
        return localPart;
    }
}
