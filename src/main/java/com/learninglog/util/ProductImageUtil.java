package com.learninglog.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * Product and vendor logo uploads — delegates to {@link ImageUtil} (external storage).
 */
public final class ProductImageUtil {

    private ProductImageUtil() {
    }

    /**
     * @return filename only (store in database); use {@link ImageUtil#buildImageUrl} in views
     */
    public static String saveUploadedPhoto(HttpServletRequest req, String partName)
            throws IOException, ServletException {
        return ImageUtil.saveUploadedFile(req, partName, "product_");
    }

    public static String saveVendorLogo(HttpServletRequest req, String partName)
            throws IOException, ServletException {
        return ImageUtil.saveUploadedFile(req, partName, "logo_");
    }
}
