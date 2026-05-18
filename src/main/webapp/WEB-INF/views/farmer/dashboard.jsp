<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Farmer Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body class="farmer-apply-body">
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<header class="farmer-apply-header">
    <div class="farmer-apply-header__inner farmer-dash-header__inner">
        <a class="farmer-apply-brand" href="${ctx}/shop">Krishak</a>
        <nav class="farmer-apply-nav" aria-label="Farmer dashboard">
            <a href="${ctx}/shop">Shop</a>
            <a href="${ctx}/about">About Us</a>
            <a href="${ctx}/farmer/apply">Application</a>
            <c:choose>
                <c:when test="${previewMode}">
                    <a href="${ctx}/login">Sign in</a>
                </c:when>
                <c:otherwise>
                    <a href="${ctx}/login">Sign out</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>

<main class="farmer-apply-main">
    <div class="farmer-apply-page farmer-dash-page">
        <c:if test="${previewMode}">
            <p class="farmer-dash-preview-banner" role="status">
                Preview mode — not logged in. After login, use <a href="${ctx}/farmer/dashboard">/farmer/dashboard</a>.
            </p>
        </c:if>
        <header class="vendor-form-page__intro">
            <p class="farmer-dash-eyebrow">Farmer portal</p>
            <h1 class="vendor-form-page__title">Welcome, <c:out value="${vendorName}" /></h1>
            <p class="vendor-form-page__subtitle">
                Manage your listings, track orders, and keep your farm visible to customers on Krishak.
            </p>
        </header>

        <section class="farmer-dash-stats" aria-label="Overview">
            <article class="farmer-dash-stat">
                <span class="farmer-dash-stat__label">Products</span>
                <span class="farmer-dash-stat__value">—</span>
                <span class="farmer-dash-stat__hint">Coming soon</span>
            </article>
            <article class="farmer-dash-stat">
                <span class="farmer-dash-stat__label">Open orders</span>
                <span class="farmer-dash-stat__value">—</span>
                <span class="farmer-dash-stat__hint">Coming soon</span>
            </article>
            <article class="farmer-dash-stat">
                <span class="farmer-dash-stat__label">Shop status</span>
                <span class="farmer-dash-stat__value">Active</span>
                <span class="farmer-dash-stat__hint">Vendor account</span>
            </article>
        </section>

        <section class="farmer-dash-actions" aria-labelledby="farmer-dash-actions-heading">
            <h2 id="farmer-dash-actions-heading" class="farmer-dash-section-title">Quick actions</h2>
            <div class="farmer-dash-grid">
                <a class="farmer-dash-card" href="${ctx}/farmer/products">
                    <span class="farmer-dash-card__icon" aria-hidden="true">
                        <img src="${ctx}/image/box.png" alt="" width="28" height="28" />
                    </span>
                    <span class="farmer-dash-card__title">Manage products</span>
                    <span class="farmer-dash-card__desc">Add or update what you sell on the marketplace.</span>
                </a>
                <a class="farmer-dash-card" href="${ctx}/farmer/orders">
                    <span class="farmer-dash-card__icon" aria-hidden="true">
                        <img src="${ctx}/image/white_cart.png" alt="" width="28" height="28" />
                    </span>
                    <span class="farmer-dash-card__title">View orders</span>
                    <span class="farmer-dash-card__desc">See customer orders for your farm.</span>
                </a>
                <a class="farmer-dash-card" href="${ctx}/farmer/apply">
                    <span class="farmer-dash-card__icon" aria-hidden="true">
                        <img src="${ctx}/image/leaf.png" alt="" width="28" height="28" />
                    </span>
                    <span class="farmer-dash-card__title">Seller application</span>
                    <span class="farmer-dash-card__desc">Update your farm details or application info.</span>
                </a>
                <a class="farmer-dash-card farmer-dash-card--muted" href="${ctx}/shop">
                    <span class="farmer-dash-card__icon" aria-hidden="true">
                        <img src="${ctx}/image/search.png" alt="" width="28" height="28" />
                    </span>
                    <span class="farmer-dash-card__title">Browse shop</span>
                    <span class="farmer-dash-card__desc">See how customers view the marketplace.</span>
                </a>
            </div>
        </section>
    </div>
</main>
</body>
</html>
