<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>${product.name} — Krishak</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/landing.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/product.css" />
  <style>
    body.with-store-header { padding-top: 72px; }
  </style>
</head>
<body class="with-store-header">

<%@ include file="/WEB-INF/views/store/header.jspf" %>

<main class="page">
  <div class="wrap">

    <section class="card media" aria-label="Product images">
      <div class="hero">
        <img id="hero-img" class="hero-main-img"
             src="${productImageUrl}" alt="${product.name}" />
        <div class="badges" aria-hidden="true">
          <c:if test="${product.organic}">
            <span class="badge badge-green">
              <img class="badge-icon" src="${pageContext.request.contextPath}/image/leaf.svg"
                   alt="" aria-hidden="true" />
              Organic
            </span>
          </c:if>
        </div>
      </div>
    </section>

    <section class="details" aria-label="Product details">
      <p class="category">${product.category}</p>
      <h1 class="title">${product.name}</h1>

      <div class="rating-row">
        <div class="stars" aria-label="Rating"></div>
        <p class="rating-text">
          <span>4.8</span>
          <span class="muted">(127 reviews)</span>
        </p>
      </div>

      <div class="price-row">
        <span class="price">Rs. <fmt:formatNumber value="${product.price}" maxFractionDigits="2" groupingUsed="false"/></span>
        <span class="unit muted">/ ${product.unit}</span>
      </div>

      <div class="stock-row">
        <c:choose>
          <c:when test="${product.stock <= 0}">
            <span class="pill out">Out of Stock</span>
          </c:when>
          <c:when test="${product.stock <= 5}">
            <span class="pill">Limited Stock</span>
          </c:when>
          <c:otherwise>
            <span class="pill">In Stock</span>
          </c:otherwise>
        </c:choose>
      </div>

      <p class="desc">${product.description}</p>

      <div class="specs">
        <div class="specs-grid">
          <div class="spec">
            <span class="spec-k">Vendor:</span>
            <span class="spec-v">${product.vendorDisplayName}</span>
          </div>
          <div class="spec">
            <span class="spec-k">Stock:</span>
            <span class="spec-v">${product.stock} ${product.unit}</span>
          </div>
          <div class="spec">
            <span class="spec-k">Type:</span>
            <span class="spec-v">${product.organic ? 'Organic' : 'Regular'}</span>
          </div>
        </div>
      </div>

      <c:choose>
        <c:when test="${product.stock > 0}">
          <form id="add-cart-form" method="post" action="${pageContext.request.contextPath}/cart" class="buy-row">
            <input type="hidden" name="action" value="add" />
            <input type="hidden" name="productId" value="${product.id}" />
            <input type="hidden" name="quantity" id="qtyField" value="1" />
            <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/product?id=${product.id}" />

            <div class="qty-wrap">
              <span class="qty-label">Quantity:</span>
              <div class="qty" role="group" aria-label="Quantity">
                <button type="button" id="qty-minus" aria-label="Decrease quantity">−</button>
                <span id="qty-value">1</span>
                <button type="button" id="qty-plus" aria-label="Increase quantity">+</button>
              </div>
            </div>

            <button id="add-btn" class="add-btn" type="submit">
              <img src="${pageContext.request.contextPath}/image/white_cart.svg" alt="" aria-hidden="true" />
              Add to Cart
            </button>

            <button id="wish-btn" class="wish-btn" type="button" aria-label="Wishlist (demo)"
                    onclick="return false;">
              <img src="${pageContext.request.contextPath}/image/white_heart.svg" alt="" aria-hidden="true" />
            </button>
          </form>
        </c:when>
        <c:otherwise>
          <p class="muted">This product is currently unavailable.</p>
        </c:otherwise>
      </c:choose>

      <div class="features" aria-label="Benefits">
        <div class="feature">
          <img src="${pageContext.request.contextPath}/image/white_truck.svg" alt="" aria-hidden="true" />
          <span>Free Delivery</span>
        </div>
        <div class="feature">
          <img src="${pageContext.request.contextPath}/image/shield.png" alt="" aria-hidden="true" />
          <span>Quality Guaranteed</span>
        </div>
        <div class="feature">
          <img src="${pageContext.request.contextPath}/image/leaf.svg" alt="" aria-hidden="true" />
          <span>Fresh &amp; Organic</span>
        </div>
      </div>

      <c:if test="${not empty message}">
        <p class="toast">${message}</p>
      </c:if>

    </section>
  </div>
</main>

<%@ include file="/WEB-INF/views/store/footer.jspf" %>

<script>
(function () {
  var maxQ = ${product.stock};
  var q = 1;
  var field = document.getElementById('qtyField');
  var label = document.getElementById('qty-value');
  var minus = document.getElementById('qty-minus');
  var plus = document.getElementById('qty-plus');
  if (!field || !label || !minus || !plus) return;

  function paint() {
    field.value = String(q);
    label.textContent = String(q);
    minus.disabled = q <= 1;
    plus.disabled = q >= maxQ;
  }

  minus.addEventListener('click', function () {
    q = Math.max(1, q - 1);
    paint();
  });
  plus.addEventListener('click', function () {
    q = Math.min(maxQ, q + 1);
    paint();
  });
  paint();
})();
</script>
</body>
</html>
