package com.learninglog.model;

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

    /** Text shown in customer preview when shop bio is empty. */
    public String getPreviewBio() {
        if (shopBio != null && !shopBio.isBlank()) {
            return shopBio.trim();
        }
        return "Your trusted source for fresh, organic produce. We work directly with local farmers "
                + "to bring you the highest quality fruits and vegetables.";
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

    /** Address line in customer preview. */
    public String getPreviewAddress() {
        if (address != null && !address.isBlank()) {
            return address.trim();
        }
        return "1234 Farm Road, Green Valley, CA 94000";
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
