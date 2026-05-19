package com.learninglog.dao;

import java.util.List;

public interface OrderDao {

    /**
     * Creates an order with status {@code pending} and line items.
     *
     * @return new order id, or -1 on failure
     */
    int createOrder(int customerUserId, List<CheckoutLine> lines);

    record CheckoutLine(int productId, int quantity) {
    }
}
