<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Shopping Cart — Krishak</title>
  <base href="${pageContext.request.contextPath}/" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/landing.css" />
</head>
<body>
  <jsp:include page="/WEB-INF/views/customer/navbar.jsp">
    <jsp:param name="current" value="product" />
  </jsp:include>

  <main class="page">
    <div class="layout">
      <section class="cart" aria-label="Cart items">
        <h1 class="cart-page-title">Shopping Cart</h1>
        <p id="cart-count" class="cart-count">0 items in cart</p>
        <div id="cart-list" class="cart-list"></div>
      </section>

      <aside class="summary" aria-label="Order summary">
        <div class="summary-card">
          <h2 class="summary-title">Order Summary</h2>
          <dl class="summary-lines">
            <div class="line total">
              <dt>Subtotal</dt>
              <dd id="summary-subtotal">Rs. 0.00</dd>
            </div>
            <div class="line total">
              <dt>Total</dt>
              <dd id="summary-total">Rs. 0.00</dd>
            </div>
          </dl>
          <button id="checkout-btn" class="checkout-btn" type="button">Proceed to Checkout</button>
          <p class="free-note"><a href="${pageContext.request.contextPath}/product">Continue shopping</a></p>
        </div>
      </aside>
    </div>
  </main>

  <script>window.CART_CTX = "${pageContext.request.contextPath}";</script>
  <script src="${pageContext.request.contextPath}/cart.js"></script>
</body>
</html>
