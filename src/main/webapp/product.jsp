<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Temporary stub — remove when ProductServlet is ready
    request.setAttribute("product", new com.learninglog.entity.Product(
        "Fresh Tomato", "Vegetables", 120, "kg", 50, true,
        "Farmer Ram", "image/fresh_tomato.png", "Sweet and ripe."
    ));
%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>${product.name} - Product Details</title>
  <link rel="stylesheet" href="product.css" />
</head>
<body>
<header class="topbar">
  <div class="topbar-inner">
    <a class="back" href="shop.jsp" aria-label="Back to shop">
      <span aria-hidden="true">←</span>
      <span class="back-text">Back</span>
    </a>
    <div class="top-actions">
      <a class="icon-btn" href="cart.jsp" aria-label="Open cart">
        <img src="image/cart.png" alt="" aria-hidden="true" />
      </a>
    </div>
  </div>
</header>

<main class="page">
  <div class="wrap">

    <section class="card media" aria-label="Product images">
      <div class="hero">
        <img id="hero-img" class="hero-main-img"
             src="${product.photoPath}" alt="${product.name}" />
        <div class="badges" aria-hidden="true">
          <c:if test="${product.organic}">
              <span class="badge badge-green">
                <img class="badge-icon" src="image/white_leaf.png"
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

      <%-- TODO: Replace with real rating from Reviews table --%>
      <div class="rating-row">
        <div class="stars" aria-label="Rating"></div>
        <p class="rating-text">
          <span>4.8</span>
          <span class="muted">(127 reviews)</span>
        </p>
      </div>

      <div class="price-row">
        <span class="price">Rs ${product.price}</span>
        <span class="unit muted">/ ${product.unit}</span>
      </div>

      <div class="stock-row">
          <span class="pill ${product.stock > 0 ? '' : 'out'}">
            ${product.stock > 0 ? "In Stock" : "Out of Stock"}
          </span>
      </div>

      <p class="desc">${product.description}</p>

      <div class="specs">
        <div class="specs-grid">
          <div class="spec">
            <span class="spec-k">Farm:</span>
            <span class="spec-v">${product.farmerName}</span>
          </div>
          <div class="spec">
            <span class="spec-k">Stock:</span>
            <span class="spec-v">${product.stock} ${product.unit}</span>
          </div>
          <div class="spec">
            <span class="spec-k">Type:</span>
            <span class="spec-v">${product.organic ? "Organic" : "Regular"}</span>
          </div>
        </div>
      </div>

      <%-- Quantity — simple for now, cart decision pending --%>
      <div class="buy-row">
        <div class="qty-wrap">
          <span class="qty-label">Quantity:</span>
          <div class="qty" role="group" aria-label="Quantity">
            <button type="button" aria-label="Decrease quantity">-</button>
            <span id="qty-value">1</span>
            <button type="button" aria-label="Increase quantity">+</button>
          </div>
        </div>

        <%-- TODO: Wire to CartServlet once cart decision is made --%>
        <button id="add-btn" class="add-btn" type="button">
          <img src="image/white_cart.png" alt="" aria-hidden="true" />
          Add to Cart
        </button>

        <button id="wish-btn" class="wish-btn" type="button"
                aria-label="Add to wishlist">
          <img src="image/heart.png" alt="" aria-hidden="true" />
        </button>
      </div>

      <div class="features" aria-label="Benefits">
        <div class="feature">
          <img src="image/truck.png" alt="" aria-hidden="true" />
          <span>Free Delivery</span>
        </div>
        <div class="feature">
          <img src="image/shield.png" alt="" aria-hidden="true" />
          <span>Quality Guaranteed</span>
        </div>
        <div class="feature">
          <img src="image/leaf.png" alt="" aria-hidden="true" />
          <span>Fresh &amp; Organic</span>
        </div>
      </div>

      <%-- Success/error message from Servlet --%>
      <c:if test="${not empty message}">
        <p class="toast">${message}</p>
      </c:if>

    </section>
  </div>
</main>

</body>
</html>