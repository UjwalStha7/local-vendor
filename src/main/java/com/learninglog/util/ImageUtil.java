package com.learninglog.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

/**
 * Stores uploads outside the project; database keeps filename only.
 */
public final class ImageUtil {

    public static final String UPLOAD_DIR_NAME = "local-vendor-uploads";

    private static final Set<String> ALLOWED_EXT = Set.of(".jpg", ".jpeg", ".png", ".webp");

    private ImageUtil() {
    }

    public static Path getUploadDirectory() {
        String home = System.getProperty("user.home");
        Path dir = Paths.get(home, UPLOAD_DIR_NAME);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload directory: " + dir, e);
        }
        return dir;
    }

    public static Path resolveStoredFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return null;
        }
        String safe = Paths.get(fileName).getFileName().toString();
        if (safe.isBlank() || safe.contains("..")) {
            return null;
        }
        Path file = getUploadDirectory().resolve(safe);
        return Files.isRegularFile(file) ? file : null;
    }

    /**
     * Saves a multipart file; returns unique filename only (not full path).
     */
    public static String saveUploadedFile(HttpServletRequest request, String partName, String namePrefix)
            throws IOException, ServletException {
        Part part = request.getPart(partName);
        if (part == null || part.getSize() == 0) {
            return null;
        }
        String submitted = part.getSubmittedFileName();
        if (submitted == null || submitted.isBlank()) {
            return null;
        }
        String ext = extension(submitted);
        if (!ALLOWED_EXT.contains(ext)) {
            throw new IllegalArgumentException("Image must be JPG, JPEG, PNG, or WEBP.");
        }
        String safeOriginal = Paths.get(submitted).getFileName().toString()
                .replaceAll("[^a-zA-Z0-9._-]", "_");
        String uniqueName = LocalDateTime.now().toString().replace(":", "-")
                + "_" + (namePrefix == null ? "" : namePrefix)
                + safeOriginal;
        if (!uniqueName.toLowerCase(Locale.ROOT).endsWith(ext)) {
            uniqueName = uniqueName + ext;
        }
        Path target = getUploadDirectory().resolve(uniqueName);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return uniqueName;
    }

    /**
     * Builds a browser URL for a stored value (filename or legacy /image/... path).
     */
    public static String buildImageUrl(String contextPath, String stored) {
        if (stored == null || stored.isBlank()) {
            return null;
        }
        String value = stored.trim();
        String ctx = contextPath == null ? "" : contextPath;
        if (value.startsWith("http://") || value.startsWith("https://")) {
            return value;
        }
        if (value.startsWith("/")) {
            return ctx + value;
        }
        return ctx + "/uploads/" + value;
    }

    private static String extension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0) {
            return "";
        }
        return filename.substring(dot).toLowerCase(Locale.ROOT);
    }
}
