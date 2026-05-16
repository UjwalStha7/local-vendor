<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Krishak — Farm Fresh to Your Table</title>
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/landing.css" />
</head>
<body>
<%@ include file="/WEB-INF/views/store/header.jspf" %>

<c:if test="${param.leadOk == '1'}">
  <p class="toast show" style="top:88px;">Thanks — we received your details.</p>
</c:if>
<c:if test="${param.leadErr == '1'}">
  <p class="toast show toast--err" style="top:88px;">Please enter a valid email and 10-digit phone number.</p>
</c:if>

<main>
  <section class="hero" aria-label="Welcome">
    <div class="hero-bg" style="background-image: url('${pageContext.request.contextPath}/image/hero.svg');"></div>
    <div class="hero-overlay"></div>
    <div class="hero-content">
      <h1>Fresh From Farm to Your Table</h1>
      <p>
        Connect directly with local farmers and enjoy the freshest organic produce delivered to your doorstep.
      </p>
      <a class="btn btn-primary btn-hero" href="${pageContext.request.contextPath}/browse?cat=all">
        Start Shopping <img class="btn-arrow" src="${pageContext.request.contextPath}/image/right_arrow.svg" alt="" width="16" height="16" />
      </a>
    </div>
  </section>

  <section class="section intro" id="categories">
    <div class="container narrow">
      <h2 class="section-title">Farm-Fresh Quality, Delivered Daily</h2>
      <p class="section-lead">
        Krishak connects you with trusted farmers so every fruit and vegetable is as fresh as the morning harvest—handled with care and delivered responsibly.
      </p>
    </div>

    <div class="container category-row">
      <article class="category-card">
        <div class="category-media">
          <img src="${pageContext.request.contextPath}/image/fruit.svg" alt="Bowl of fresh mixed fruits" loading="lazy" />
        </div>
        <div class="category-body">
          <h3>Fresh Fruits</h3>
          <p>Seasonal picks, naturally sweet and ready to enjoy—perfect for snacks, desserts, and everyday wellness.</p>
          <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/browse?cat=fruits">
            Shop Fruits <img class="btn-arrow" src="${pageContext.request.contextPath}/image/right_arrow.svg" alt="" width="14" height="14" />
          </a>
        </div>
      </article>
      <article class="category-card">
        <div class="category-media">
          <img src="${pageContext.request.contextPath}/image/organic_vegetables.svg" alt="Assorted organic vegetables" loading="lazy" />
        </div>
        <div class="category-body">
          <h3>Organic Vegetables</h3>
          <p>Leafy greens, roots, and more—grown with sustainable practices for flavor you can taste in every bite.</p>
          <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/browse?cat=vegetables">
            Shop Vegetables <img class="btn-arrow" src="${pageContext.request.contextPath}/image/right_arrow.svg" alt="" width="14" height="14" />
          </a>
        </div>
      </article>
    </div>

    <div class="container cta-wrap">
      <a class="btn btn-accent btn-lg" href="${pageContext.request.contextPath}/browse?cat=all">
        Explore All Products <img class="btn-arrow" src="${pageContext.request.contextPath}/image/right_arrow.svg" alt="" width="15" height="15" />
      </a>
    </div>
  </section>

  <section class="section featured" id="featured" aria-labelledby="featured-heading">
    <div class="container">
      <h2 id="featured-heading" class="section-title">Featured Products</h2>
      <p class="section-lead">Handpicked selections from our local farmers.</p>

      <div class="product-grid">
        <c:choose>
          <c:when test="${empty featuredProducts}">
            <p class="section-lead" style="grid-column: 1 / -1;">No products yet.</p>
          </c:when>
          <c:otherwise>
            <c:forEach var="p" items="${featuredProducts}">
              <article class="product-card">
                <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="product-card__media">
                  <c:set var="imgPath" value="${empty p.photoPath ? 'image/hero.svg' : p.photoPath}" />
                  <img src="${pageContext.request.contextPath}/${imgPath}" alt="${p.name}" loading="lazy" />
                </a>
                <div class="product-card__body">
                  <a class="product-card__title" href="${pageContext.request.contextPath}/product?id=${p.id}">${p.name}</a>
                  <p class="product-card__price">Rs. <fmt:formatNumber value="${p.price}" maxFractionDigits="2" groupingUsed="false"/></p>
                  <p class="product-card__unit">per ${p.unit}</p>
                  <form method="post" action="${pageContext.request.contextPath}/cart">
                    <input type="hidden" name="action" value="add" />
                    <input type="hidden" name="productId" value="${p.id}" />
                    <input type="hidden" name="quantity" value="1" />
                    <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/landing" />
                    <button type="submit" class="add-cart" aria-label="Add to cart">
                      <img src="${pageContext.request.contextPath}/image/white_cart.svg" alt="" />
                    </button>
                  </form>
                </div>
              </article>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </section>

  <section class="section why" id="why" aria-labelledby="why-heading">
    <div class="container">
      <h2 id="why-heading" class="why-title">Why Choose Krishak?</h2>
      <p class="why-lead">We're more than just a marketplace – we're a bridge between farmers and families</p>

      <div class="why-grid">
        <article class="why-card">
          <div class="why-icon" aria-hidden="true">
            <img class="why-icon-tone" src="${pageContext.request.contextPath}/image/leaf.svg" alt="" />
          </div>
          <h3>Farm Fresh Quality</h3>
          <p>Produce picked at peak ripeness and delivered within 24 hours for maximum freshness and nutrition.</p>
        </article>
        <article class="why-card">
          <div class="why-icon" aria-hidden="true">
            <img class="why-icon-tone" src="${pageContext.request.contextPath}/image/rupee.svg" alt="" />
          </div>
          <h3>Fair Prices</h3>
          <p>By eliminating middlemen, we ensure affordable prices for you while farmers get fair compensation.</p>
        </article>
        <article class="why-card">
          <div class="why-icon" aria-hidden="true">
            <img src="${pageContext.request.contextPath}/image/white_heart.svg" alt="" />
          </div>
          <h3>Support Local Farmers</h3>
          <p>Every purchase directly supports local farming families and helps sustain traditional agriculture.</p>
        </article>
        <article class="why-card">
          <div class="why-icon" aria-hidden="true">
            <img src="${pageContext.request.contextPath}/image/white_truck.svg" alt="" />
          </div>
          <h3>Convenient Delivery</h3>
          <p>Fresh produce delivered right to your doorstep at your preferred time slot. Simple and hassle-free.</p>
        </article>
      </div>
    </div>
  </section>

  <section class="section seller-cta-section" id="sell-vendor" aria-labelledby="seller-cta-heading">
    <div class="container">
      <div class="seller-cta">
        <div class="seller-cta__shine" aria-hidden="true"></div>
        <div class="seller-cta__copy">
          <h2 id="seller-cta-heading" class="seller-cta__title">Want to sell your product?</h2>
          <p class="seller-cta__lead">Join our network of local farmers — leave your email and phone number.</p>
        </div>
        <form class="seller-cta__form" method="post" action="${pageContext.request.contextPath}/vendor-lead">
          <div class="seller-cta__fields">
            <input
              name="email"
              type="email"
              class="seller-cta__input"
              placeholder="Enter your email"
              autocomplete="email"
              aria-label="Email"
              required
            />
            <input
              name="phone"
              type="tel"
              class="seller-cta__input"
              placeholder="Your Number"
              autocomplete="tel"
              aria-label="Phone number"
              required
            />
          </div>
          <button type="submit" class="seller-cta__submit">Submit</button>
        </form>
      </div>
    </div>
  </section>
</main>

<%@ include file="/WEB-INF/views/store/footer.jspf" %>

<style>
  .toast--err.toast.show { background: #b91c1c; }
</style>
</body>
</html>
