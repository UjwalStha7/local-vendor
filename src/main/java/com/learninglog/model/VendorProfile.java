package com.learninglog.model;

import com.learninglog.util.ImageUtil;

/**
 * Vendor shop profile for the farmer portal profile page.
 */
public class VendorProfile {

    private String shopName;
    private String shopBio;
    private String email;
    private String phone;
    private String address;
    private String logoUrl;
    private boolean verified;
    private String memberSince;
    private String responseTime;

    public VendorProfile() {
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopBio() {
        return shopBio;
    }

    public void setShopBio(String shopBio) {
        this.shopBio = shopBio;
    }

    public boolean hasShopBio() {
        return shopBio != null && !shopBio.isBlank();
    }

    /** EL: ${profile.hasShopBio} */
    public boolean getHasShopBio() {
        return hasShopBio();
    }

    public boolean hasAddress() {
        return address != null && !address.isBlank();
    }

    /** EL: ${profile.hasAddress} */
    public boolean getHasAddress() {
        return hasAddress();
    }

    public boolean hasLogo() {
        return logoUrl != null && !logoUrl.isBlank();
    }

    /** EL: ${profile.hasLogo} */
    public boolean getHasLogo() {
        return hasLogo();
    }

    /** Resolved logo URL for img src (upload servlet or legacy path). */
    public String resolveLogoSrc(String contextPath) {
        String ctx = contextPath == null ? "" : contextPath;
        if (!hasLogo()) {
            return ctx + "/image/fresh_apple.png";
        }
        String built = ImageUtil.buildImageUrl(ctx, logoUrl);
        return built != null ? built : ctx + "/image/fresh_apple.png";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }
}
