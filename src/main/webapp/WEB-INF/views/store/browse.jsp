<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Products — Krishak</title>
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/landing.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/shop.css" />
</head>
<body>
<%@ include file="/WEB-INF/views/store/header.jspf" %>

<main class="page" aria-label="Products">
  <form class="browse-form" method="get" action="${pageContext.request.contextPath}/browse">
    <section class="search-area" aria-label="Product search">
      <div class="shop-container">
        <div class="search">
          <img class="search-icon" src="${pageContext.request.contextPath}/image/search.svg" alt="" aria-hidden="true" />
          <input name="q" type="search" value="${browseParams.search}"
                 placeholder="Search for fresh fruits, vegetables, and more..." autocomplete="off" />
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
              <input type="radio" name="cat" value="all" ${browseParams.category == 'all' ? 'checked' : ''} />
              <span>All Products</span>
            </label>
            <label class="check">
              <input type="radio" name="cat" value="fruits" ${browseParams.category == 'fruits' ? 'checked' : ''} />
              <span>Fruits</span>
            </label>
            <label class="check">
              <input type="radio" name="cat" value="vegetables" ${browseParams.category == 'vegetables' ? 'checked' : ''} />
              <span>Vegetables</span>
            </label>
          </div>

          <div class="field">
            <h3 class="field-title">Price Range (Rs.)</h3>
            <p class="range-hint">Rs. <span id="priceMinLabel">0</span> — Rs. <span id="priceMaxLabel">1000</span></p>
            <div class="dual-range">
              <input type="range" id="priceSliderMin" min="0" max="1000" value="0" step="10" aria-label="Minimum price" />
              <input type="range" id="priceSliderMax" min="0" max="1000" value="1000" step="10" aria-label="Maximum price" />
            </div>
            <input type="hidden" name="minPrice" id="minPriceHidden"
                   value="${browseParams.minPrice != null ? browseParams.minPrice : ''}" />
            <input type="hidden" name="maxPrice" id="maxPriceHidden"
                   value="${browseParams.maxPrice != null ? browseParams.maxPrice : ''}" />
          </div>

          <div class="field">
            <h3 class="field-title">Vendor</h3>
            <c:choose>
              <c:when test="${empty vendorOptions}">
                <p class="muted-small">No vendors yet.</p>
              </c:when>
              <c:otherwise>
                <c:forEach var="v" items="${vendorOptions}">
                  <label class="check">
                    <input type="checkbox" name="vendorId" value="${v.vendorUserId}"
                           ${browseParams.isVendorSelected(v.vendorUserId) ? 'checked' : ''} />
                    <span>${v.displayName}</span>
                  </label>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </div>

          <div class="field">
            <h3 class="field-title">Availability</h3>
            <label class="check">
              <input type="radio" name="avail" value="all" ${browseParams.availability == 'all' ? 'checked' : ''} />
              <span>All</span>
            </label>
            <label class="check">
              <input type="radio" name="avail" value="in" ${browseParams.availability == 'in' ? 'checked' : ''} />
              <span>In Stock</span>
            </label>
            <label class="check">
              <input type="radio" name="avail" value="limited" ${browseParams.availability == 'limited' ? 'checked' : ''} />
              <span>Limited Stock</span>
            </label>
          </div>

          <div class="field">
            <h3 class="field-title">Sort By</h3>
            <select id="sortBy" name="sort" class="sort-select sort-select--block" onchange="this.form.submit()">
              <option value="featured" ${browseParams.sort == 'featured' ? 'selected' : ''}>Featured</option>
              <option value="price-asc" ${browseParams.sort == 'price-asc' ? 'selected' : ''}>Price: Low to High</option>
              <option value="price-desc" ${browseParams.sort == 'price-desc' ? 'selected' : ''}>Price: High to Low</option>
              <option value="name-asc" ${browseParams.sort == 'name-asc' ? 'selected' : ''}>Name</option>
            </select>
          </div>

          <div class="filter-actions">
            <button class="btn btn-primary" type="submit">Apply Filters</button>
          </div>
        </div>
      </aside>

      <div class="browse-results-summary">
        <div class="results-head results-head--solo">
          <p class="found">
            <c:choose>
              <c:when test="${sellableCount == 0}">0 Products Found</c:when>
              <c:otherwise>${matchingCount} Products Found</c:otherwise>
            </c:choose>
          </p>
        </div>
      </div>
    </div>
  </form>

  <div class="shop-container layout browse-products-row">
    <div class="layout-gutter" aria-hidden="true"></div>
    <section class="results results--products" aria-label="Product results">
      <c:choose>
        <c:when test="${sellableCount == 0}">
          <p class="empty-msg">No products yet</p>
        </c:when>
        <c:when test="${matchingCount == 0}">
          <p class="empty-msg">No products found</p>
        </c:when>
        <c:otherwise>
          <div class="grid" aria-label="Product grid">
            <c:forEach var="p" items="${products}">
              <article class="card">
                <c:set var="stockBadge" value="${p.stockQuantity > 5 ? 'green' : 'orange'}" />
                <c:set var="stockLabel" value="${p.stockQuantity > 5 ? 'In Stock' : 'Limited'}" />
                <a class="card-media" href="${pageContext.request.contextPath}/product?id=${p.id}">
                  <c:set var="bimg" value="${empty p.photoPath ? 'image/hero.svg' : p.photoPath}" />
                  <img src="${pageContext.request.contextPath}/${bimg}" alt="${p.name}" />
                  <span class="badge ${stockBadge == 'green' ? 'green' : ''}">${stockLabel}</span>
                </a>
                <div class="card-body">
                  <h3 class="card-title">
                    <a class="open" href="${pageContext.request.contextPath}/product?id=${p.id}">${p.name}</a>
                  </h3>
                  <p class="vendor">by ${p.vendorDisplayName}</p>
                  <div class="card-rating">
                    <span class="rating">★★★★☆ <span class="rating-num">4.5</span></span>
                  </div>
                  <div class="card-meta">
                    <div>
                      <span class="price">Rs. <fmt:formatNumber value="${p.price}" maxFractionDigits="2" groupingUsed="false"/></span>
                      <span class="unit"> / ${p.unit}</span>
                    </div>
                  </div>
                  <div class="card-actions">
                    <form method="post" action="${pageContext.request.contextPath}/cart" style="margin:0;width:100%;">
                      <input type="hidden" name="action" value="add" />
                      <input type="hidden" name="productId" value="${p.id}" />
                      <input type="hidden" name="quantity" value="1" />
                      <c:url var="redir" value="/browse">
                        <c:param name="q" value="${browseParams.search}" />
                        <c:param name="cat" value="${browseParams.category}" />
                        <c:if test="${browseParams.minPrice != null}">
                          <c:param name="minPrice" value="${browseParams.minPrice}" />
                        </c:if>
                        <c:if test="${browseParams.maxPrice != null}">
                          <c:param name="maxPrice" value="${browseParams.maxPrice}" />
                        </c:if>
                        <c:forEach var="vid" items="${browseParams.vendorUserIds}">
                          <c:param name="vendorId" value="${vid}" />
                        </c:forEach>
                        <c:param name="avail" value="${browseParams.availability}" />
                        <c:param name="sort" value="${browseParams.sort}" />
                        <c:param name="page" value="${browseParams.page}" />
                      </c:url>
                      <input type="hidden" name="redirect" value="${pageContext.request.contextPath}${redir}" />
                      <button type="submit" class="btn btn-primary" style="width:100%;">
                        <img src="${pageContext.request.contextPath}/image/white_cart.svg" alt="" /> Add to Cart
                      </button>
                    </form>
                  </div>
                </div>
              </article>
            </c:forEach>
          </div>

          <nav class="pager" aria-label="Pagination">
            <c:if test="${browseParams.page > 1}">
              <a class="btn btn-ghost" href="${pageContext.request.contextPath}/browse?${queryPrefix}page=${browseParams.page - 1}">Previous</a>
            </c:if>
            <div class="pager-pages">
              <c:forEach var="pn" begin="1" end="${totalPages}">
                <c:choose>
                  <c:when test="${pn == browseParams.page}">
                    <span class="page-link active">${pn}</span>
                  </c:when>
                  <c:otherwise>
                    <a class="page-link" href="${pageContext.request.contextPath}/browse?${queryPrefix}page=${pn}">${pn}</a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </div>
            <c:if test="${browseParams.page < totalPages}">
              <a class="btn btn-ghost" href="${pageContext.request.contextPath}/browse?${queryPrefix}page=${browseParams.page + 1}">Next</a>
            </c:if>
          </nav>
        </c:otherwise>
      </c:choose>
    </section>
  </div>
</main>

<%@ include file="/WEB-INF/views/store/footer.jspf" %>

<script>
(function () {
  var smin = document.getElementById('priceSliderMin');
  var smax = document.getElementById('priceSliderMax');
  var hmin = document.getElementById('minPriceHidden');
  var hmax = document.getElementById('maxPriceHidden');
  var labMin = document.getElementById('priceMinLabel');
  var labMax = document.getElementById('priceMaxLabel');
  if (!smin || !smax || !hmin || !hmax) return;

  function clampSwapped() {
    var a = parseInt(smin.value, 10);
    var b = parseInt(smax.value, 10);
    if (a > b) {
      smin.value = b;
      smax.value = a;
    }
  }

  function syncFromServer() {
    var vmin = parseFloat(hmin.value);
    var vmax = parseFloat(hmax.value);
    if (!isNaN(vmin) && vmin >= 0) smin.value = String(Math.min(1000, vmin));
    if (!isNaN(vmax) && vmax >= 0) smax.value = String(Math.min(1000, vmax));
    if (isNaN(vmin) || hmin.value === '') smin.value = '0';
    if (isNaN(vmax) || hmax.value === '') smax.value = '1000';
    clampSwapped();
    paint();
  }

  function paint() {
    clampSwapped();
    labMin.textContent = smin.value;
    labMax.textContent = smax.value;
    hmin.value = smin.value === '0' ? '' : smin.value;
    hmax.value = smax.value === '1000' ? '' : smax.value;
  }

  smin.addEventListener('input', paint);
  smax.addEventListener('input', paint);
  syncFromServer();
})();
</script>
</body>
</html>
