package com.learninglog.util;

import jakarta.servlet.http.HttpSession;

import java.util.LinkedHashMap;
import java.util.Map;

/** Session-backed shopping cart: productId -&gt; quantity. */
public final class CartUtil {

    public static final String SESSION_ATTR = "krishakCart";

    private CartUtil() {
    }

    @SuppressWarnings("unchecked")
    public static LinkedHashMap<Integer, Integer> getCart(HttpSession session) {
        Object o = session.getAttribute(SESSION_ATTR);
        if (o instanceof LinkedHashMap) {
            return (LinkedHashMap<Integer, Integer>) o;
        }
        LinkedHashMap<Integer, Integer> fresh = new LinkedHashMap<>();
        session.setAttribute(SESSION_ATTR, fresh);
        return fresh;
    }

    public static int totalItems(Map<Integer, Integer> cart) {
        int n = 0;
        for (int q : cart.values()) {
            n += q;
        }
        return n;
    }

    public static void clear(HttpSession session) {
        session.removeAttribute(SESSION_ATTR);
    }
}
