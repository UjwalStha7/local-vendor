package com.learninglog.filter;

import com.learninglog.util.CartUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/** Ensures storefront JSPs always receive cartCount (and context path) for the shared header. */
@WebFilter(urlPatterns = {"/landing", "/browse", "/product", "/cart", "/product.jsp"})
public class StorefrontAttributeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        int count = CartUtil.totalItems(CartUtil.getCart(session));
        req.setAttribute("cartCount", count);
        chain.doFilter(request, response);
    }
}
