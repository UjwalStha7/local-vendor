package com.learninglog.model;

import com.learninglog.entity.Product;

/** One row on the shopping cart screen. */
public class CartLineItem {
    private final Product product;
    private final int quantity;

    public CartLineItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }
}
