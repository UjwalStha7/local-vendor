package com.learninglog.model;

/**
 * One vendor shown on the admin Vendor Accounts page.
 */
public class VendorAccountCard {

    private final int id;
    private final String name;
    private final String company;
    private final String email;
    private final String phone;
    private final int products;
    private final int orders;
    private final boolean verified;

    public VendorAccountCard(int id, String name, String company, String email, String phone,
                             int products, int orders, boolean verified) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.email = email;
        this.phone = phone;
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
