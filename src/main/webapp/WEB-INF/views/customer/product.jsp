<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Products — Krishak</title>
  <base href="${pageContext.request.contextPath}/" />
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/landing.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css" />
</head>
<body class="page-shop">
  <jsp:include page="/WEB-INF/views/customer/navbar.jsp">
    <jsp:param name="current" value="product" />
  </jsp:include>

  <main class="page" aria-label="Products">
    <section class="search-area" aria-label="Product search">
      <div class="shop-container">
        <div class="search">
          <img class="search-icon" src="image/search.png" alt="" aria-hidden="true" />
          <input id="q" type="search" placeholder="Search for fresh fruits, vegetables and more..." autocomplete="off" />
        </div>
      </div>
    </section>
    <div class="shop-container layout">
      <aside class="filters" aria-label="Filters">
        <div class="panel">
          <h2 class="panel-title">Filters</h2>

          <div class="field">
            <h3 class="field-title">Category</h3>
            <label class="check">
              <input type="checkbox" name="cat" value="all" checked />
              <span>All Products</span>
            </label>
            <label class="check">
              <input type="checkbox" name="cat" value="Fruits" />
              <span>Fruits</span>
            </label>
            <label class="check">
              <input type="checkbox" name="cat" value="Vegetables" />
              <span>Vegetables</span>
            </label>
          </div>

          <div class="field">
            <h3 class="field-title">Price Range</h3>
            <div class="range-row">
              <input id="priceMin" type="number" inputmode="numeric" placeholder="Min" />
              <span class="range-sep">-</span>
              <input id="priceMax" type="number" inputmode="numeric" placeholder="Max" />
            </div>
          </div>

          <div class="field" id="vendorFilters">
            <h3 class="field-title">Vendor</h3>
            <p class="field-hint" id="vendorFiltersHint">Loading vendors…</p>
          </div>

          <div class="field">
            <h3 class="field-title">Availability</h3>
            <label class="check">
              <input type="radio" name="avail" value="all" checked />
              <span>All</span>
            </label>
            <label class="check">
              <input type="radio" name="avail" value="in" />
              <span>In Stock</span>
            </label>
            <label class="check">
              <input type="radio" name="avail" value="limited" />
              <span>Limited Stock</span>
            </label>
          </div>

          <div class="filter-actions">
            <button id="applyBtn" class="btn btn-primary" type="button">Apply Filters</button>
          </div>
        </div>
      </aside>

      <section class="results" aria-label="Product results">
        <div class="results-head">
          <p class="found"><span id="foundCount">0</span> Products Found</p>
          <div class="sort">
            <label class="sort-label" for="sortBy">Sort By:</label>
            <select id="sortBy" class="sort-select">
              <option value="featured">Featured</option>
              <option value="price-asc">Price: Low to High</option>
              <option value="price-desc">Price: High to Low</option>
              <option value="rating-desc">Rating</option>
              <option value="name-asc">Name</option>
            </select>
          </div>
        </div>

        <div id="grid" class="grid" aria-label="Product grid"></div>

        <div class="pager" aria-label="Pagination">
          <button id="prevBtn" class="btn btn-ghost" type="button">Previous</button>
          <button id="pageBtn" class="page-pill" type="button" aria-current="page">1</button>
          <button id="nextBtn" class="btn btn-ghost" type="button">Next</button>
        </div>
      </section>
    </div>
  </main>

  <footer class="site-footer">
    <div class="footer-grid container">
      <div class="footer-col">
        <h3>About Krishak</h3>
        <p>
          Connecting farmers directly with consumers, bringing fresh, organic produce straight from the farm to your table.
        </p>
      </div>
      <div class="footer-col">
        <h3>Quick Links</h3>
        <ul class="footer-links">
          <jsp:include page="/WEB-INF/views/customer/footer-quicklinks.jsp" />
        </ul>
      </div>
      <div class="footer-col">
        <h3 class="footer-title-link"><a href="${pageContext.request.contextPath}/contact">Contact Us</a></h3>
        <ul class="footer-contact">
          <li>
            <img src="image/phone.png" alt="" width="18" height="18" />
            <a href="tel:+9779876543210">+977 9876543210</a>
          </li>
          <li>
            <img src="image/green_email.png" alt="" width="18" height="18" />
            <a href="mailto:support@krishak.com">support@krishak.np</a>
          </li>
          <li>
            <img src="image/location.png" alt="" width="18" height="18" />
            <span>12 Matepani, Pokhara, Gandaki 33700</span>
          </li>
        </ul>
      </div>
      <div class="footer-col">
        <h3>Follow Us</h3>
        <div class="social-row" aria-label="Social media">
          <a class="social-btn" href="#" aria-label="Facebook"><img src="image/facebook.png" alt="" /></a>
          <a class="social-btn" href="#" aria-label="Twitter"><img src="image/bird.png" alt="" /></a>
          <a class="social-btn" href="#" aria-label="Instagram"><img src="image/instagram.png" alt="" /></a>
          <a class="social-btn" href="#" aria-label="LinkedIn"><img src="image/linkedIn.png" alt="" /></a>
        </div>
      </div>
    </div>

    <div class="footer-bar">
      <div class="container footer-bar-inner">
        <p>© 2026 Krishak. All rights Reserved. | <a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a></p>
      </div>
    </div>
  </footer>

  <script src="shop.js"></script>
</body>
</html>

