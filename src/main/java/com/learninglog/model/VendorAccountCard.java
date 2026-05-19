package com.learninglog.model;

import com.learninglog.util.ImageUtil;

/**
 * One vendor shown on the admin Vendor Accounts page.
 */
public class VendorAccountCard {

    private final int id;
    private final String name;
    private final String company;
    private final String email;
    private final String phone;
    private final String shopBio;
    private final String address;
    private final String logoPath;
    private final int products;
    private final int orders;
    private final boolean verified;

    public VendorAccountCard(int id, String name, String company, String email, String phone,
                             String shopBio, String address, String logoPath,
                             int products, int orders, boolean verified) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.email = email;
        this.phone = phone;
        this.shopBio = shopBio;
        this.address = address;
        this.logoPath = logoPath;
        this.products = products;
        this.orders = orders;
        this.verified = verified;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getShopBio() {
        return shopBio;
    }

    public String getAddress() {
        return address;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public boolean hasShopBio() {
        return shopBio != null && !shopBio.isBlank();
    }

    public boolean getHasShopBio() {
        return hasShopBio();
    }

    public boolean hasAddress() {
        return address != null && !address.isBlank();
    }

    public boolean getHasAddress() {
        return hasAddress();
    }

    public boolean hasLogo() {
        return logoPath != null && !logoPath.isBlank();
    }

    public boolean getHasLogo() {
        return hasLogo();
    }

    public String resolveLogoSrc(String contextPath) {
        String ctx = contextPath == null ? "" : contextPath;
        if (!hasLogo()) {
            return ctx + "/image/fresh_apple.png";
        }
        String built = ImageUtil.buildImageUrl(ctx, logoPath);
        return built != null ? built : ctx + "/image/fresh_apple.png";
    }

    public int getProducts() {
        return products;
    }

    public int getOrders() {
        return orders;
    }

    public boolean isVerified() {
        return verified;
    }
}
