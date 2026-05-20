<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Product Details — Krishak</title>
  <base href="${pageContext.request.contextPath}/" />
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/landing.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css" />
</head>
<body class="product-detail-page">
  <jsp:include page="/WEB-INF/views/customer/navbar.jsp">
    <jsp:param name="current" value="product" />
  </jsp:include>

  <main class="page" id="product-main" hidden>
    <div class="wrap">
      <section class="card media" aria-label="Product image">
        <div class="product-hero">
          <img id="hero-img" class="product-hero-img" src="" alt="" />
        </div>
      </section>

      <section class="details" aria-label="Product details">
        <p id="category" class="category"></p>
        <h1 id="title" class="title"></h1>

        <div class="rating-row">
          <div class="stars" aria-label="Rating 5.0"></div>
          <p class="rating-text">
            <span id="rating-value">5.0</span>
            <span class="muted">(reviews coming soon)</span>
          </p>
        </div>

        <div class="price-row">
          <span id="price" class="price"></span>
          <span id="unit" class="unit muted"></span>
        </div>

        <div class="stock-row">
          <span id="stock-pill" class="pill">In Stock</span>
        </div>

        <p id="desc" class="desc"></p>

        <div class="specs">
          <div class="specs-grid">
            <div class="spec">
              <span class="spec-k">Vendor:</span>
              <span id="vendor" class="spec-v"></span>
            </div>
            <div class="spec">
              <span class="spec-k">Unit:</span>
              <span id="unit-spec" class="spec-v"></span>
            </div>
            <div class="spec">
              <span class="spec-k">Category:</span>
              <span id="type" class="spec-v"></span>
            </div>
            <div class="spec">
              <span class="spec-k">Available:</span>
              <span id="stock-spec" class="spec-v"></span>
            </div>
          </div>
        </div>

        <div class="buy-row">
          <div class="qty-wrap">
            <span class="qty-label">Quantity:</span>
            <div class="qty" role="group" aria-label="Quantity">
              <button id="qty-dec" type="button" aria-label="Decrease quantity">-</button>
              <span id="qty-value" aria-live="polite">1</span>
              <button id="qty-inc" type="button" aria-label="Increase quantity">+</button>
            </div>
          </div>

          <button id="add-btn" class="add-btn" type="button">
            <img src="image/white_cart.png" alt="" aria-hidden="true" />
            Add to Cart
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

        <p id="toast" class="toast" role="status" aria-live="polite"></p>
      </section>
    </div>
  </main>

  <main class="page" id="product-error" hidden>
    <div class="wrap" style="display:block;max-width:720px;padding:40px 18px;text-align:center;">
      <h1 class="title" style="font-size:24px;">Product not found</h1>
      <p class="desc" style="max-width:none;">This product may have been removed or is no longer available.</p>
      <a class="add-btn" style="display:inline-flex;width:auto;padding:0 24px;text-decoration:none;margin-top:12px;" href="${pageContext.request.contextPath}/product">Back to products</a>
    </div>
  </main>

  <script src="product.js"></script>
</body>
</html>
