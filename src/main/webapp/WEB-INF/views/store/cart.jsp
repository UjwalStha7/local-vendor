<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Shopping Cart — Krishak</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/landing.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/cart.css" />
  <style>
    .cart-toolbar {
      max-width: 980px;
      margin: 0 auto;
      padding: 12px 18px 0;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
    }
    .cart-toolbar .back-arr {
      font-size: 22px;
      font-weight: 800;
      text-decoration: none;
      color: #374151;
      padding: 6px 10px;
      border-radius: 10px;
    }
    .cart-toolbar .back-arr:hover { background: #f3f4f6; }
    .thank-banner {
      max-width: 980px;
      margin: 12px auto;
      padding: 12px 16px;
      border-radius: 10px;
      background: #ecfdf5;
      border: 1px solid #6ee7b7;
      color: #065f46;
      font-weight: 700;
      text-align: center;
    }
    .cart-empty {
      max-width: 980px;
      margin: 24px auto;
      padding: 18px;
      text-align: center;
      color: #6b7280;
      font-weight: 600;
    }
    .qty form { display: contents; }
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/store/header.jspf" %>

<c:if test="${thankYou}">
  <p class="thank-banner">Thank you for your purchase!</p>
</c:if>

<div class="cart-toolbar">
  <div class="brand">
    <img class="brand-icon" src="${pageContext.request.contextPath}/image/cart.svg" alt="" aria-hidden="true" />
    <span class="brand-title">Shopping Cart</span>
  </div>
  <a class="back-arr" href="javascript:history.back()" aria-label="Back to previous page">←</a>
</div>

<main class="page">
  <div class="layout">
    <section class="cart" aria-label="Cart items">
      <p class="cart-count">${cartCount} Items in Cart</p>

      <c:choose>
        <c:when test="${empty cartLines}">
          <p class="cart-empty">Your cart is empty.</p>
        </c:when>
        <c:otherwise>
          <div class="cart-list">
            <c:forEach var="line" items="${cartLines}">
              <article class="item">
                <div class="item-img">
                  <c:set var="cimg" value="${empty line.product.photoPath ? 'image/hero.svg' : line.product.photoPath}" />
                  <img src="${pageContext.request.contextPath}/${cimg}" alt="${line.product.name}" />
                </div>
                <div class="item-main">
                  <p class="item-title">${line.product.name}</p>
                  <p class="item-sub">
                    Rs. <fmt:formatNumber value="${line.product.price}" maxFractionDigits="2" groupingUsed="false"/> / ${line.product.unit}
                  </p>
                  <div class="item-actions">
                    <div class="qty">
                      <c:choose>
                        <c:when test="${line.quantity > 1}">
                          <form method="post" action="${pageContext.request.contextPath}/cart" style="display:inline;margin:0;padding:0;">
                            <input type="hidden" name="action" value="update" />
                            <input type="hidden" name="productId" value="${line.product.id}" />
                            <input type="hidden" name="quantity" value="${line.quantity - 1}" />
                            <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/cart" />
                            <button type="submit" aria-label="Decrease quantity">−</button>
                          </form>
                        </c:when>
                        <c:otherwise>
                          <button type="button" disabled aria-disabled="true">−</button>
                        </c:otherwise>
                      </c:choose>
                      <span>${line.quantity}</span>
                      <c:choose>
                        <c:when test="${line.quantity < line.product.stockQuantity}">
                          <form method="post" action="${pageContext.request.contextPath}/cart" style="display:inline;margin:0;padding:0;">
                            <input type="hidden" name="action" value="update" />
                            <input type="hidden" name="productId" value="${line.product.id}" />
                            <input type="hidden" name="quantity" value="${line.quantity + 1}" />
                            <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/cart" />
                            <button type="submit" aria-label="Increase quantity">+</button>
                          </form>
                        </c:when>
                        <c:otherwise>
                          <button type="button" disabled aria-disabled="true">+</button>
                        </c:otherwise>
                      </c:choose>
                    </div>

                    <form method="post" action="${pageContext.request.contextPath}/cart" style="display:inline;margin:0;">
                      <input type="hidden" name="action" value="remove" />
                      <input type="hidden" name="productId" value="${line.product.id}" />
                      <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/cart" />
                      <button type="submit" class="remove-btn" aria-label="Remove">🗑</button>
                    </form>
                  </div>
                </div>
                <div class="item-price">
                  Rs. <fmt:formatNumber value="${line.lineTotal}" maxFractionDigits="2" groupingUsed="false"/>
                </div>
              </article>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>
    </section>

    <aside class="summary" aria-label="Order summary">
      <div class="summary-card">
        <h2 class="summary-title">Order Summary</h2>

        <dl class="summary-lines">
          <div class="line">
            <dt>Subtotal</dt>
            <dd>Rs. <fmt:formatNumber value="${subtotal}" maxFractionDigits="2" groupingUsed="false"/></dd>
          </div>
          <div class="line">
            <dt>Tax (13%)</dt>
            <dd>Rs. <fmt:formatNumber value="${tax}" maxFractionDigits="2" groupingUsed="false"/></dd>
          </div>
          <div class="line">
            <dt>Delivery Fee</dt>
            <dd>Rs. <fmt:formatNumber value="${delivery}" maxFractionDigits="2" groupingUsed="false"/></dd>
          </div>
          <div class="line total">
            <dt>Total</dt>
            <dd>Rs. <fmt:formatNumber value="${total}" maxFractionDigits="2" groupingUsed="false"/></dd>
          </div>
        </dl>

        <c:if test="${not empty cartLines}">
          <form method="post" action="${pageContext.request.contextPath}/cart">
            <input type="hidden" name="action" value="checkout" />
            <button type="submit" class="checkout-btn">Proceed to Checkout</button>
          </form>
        </c:if>
        <p class="free-note">Free delivery on orders over Rs. 5000</p>
      </div>
    </aside>
  </div>
</main>

<%@ include file="/WEB-INF/views/store/footer.jspf" %>
</body>
</html>
