<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Vendor Portal — Dashboard</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/farmer-dashboard.css" />
</head>
<body class="vp-body">
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="activeNav" value="dashboard" scope="request" />
<c:if test="${empty storeName}"><c:set var="storeName" value="${vendorName}" /></c:if>
<c:if test="${empty storeName}"><c:set var="storeName" value="FreshHarvest Farms" /></c:if>

<div class="vp-shell">
    <jsp:include page="vendor-sidebar.jsp" />

    <div class="vp-main">
        <jsp:include page="vendor-topbar.jsp" />

        <main class="vp-content">
            <c:if test="${previewMode}">
                <p class="vp-preview-banner" role="status">
                    Preview mode — not logged in. Sign in as a vendor to use the live dashboard at
                    <a href="${ctx}/farmer/dashboard">/farmer/dashboard</a>.
                </p>
            </c:if>

            <header class="vp-page-head">
                <h1>Dashboard Overview</h1>
                <p>Welcome back! Here's what's happening with your store today.</p>
            </header>

            <section class="vp-stats" aria-label="Store metrics">
                <article class="vp-stat-card">
                    <div>
                        <p class="vp-stat-card__label">Total Products Listed</p>
                        <p class="vp-stat-card__value">10</p>
                    </div>
                    <span class="vp-stat-card__icon vp-stat-card__icon--blue" aria-hidden="true">
                        <img src="${ctx}/image/box.png" alt="" width="24" height="24" />
                    </span>
                </article>
                <article class="vp-stat-card">
                    <div>
                        <p class="vp-stat-card__label">Active Orders</p>
                        <p class="vp-stat-card__value">6</p>
                    </div>
                    <span class="vp-stat-card__icon vp-stat-card__icon--purple" aria-hidden="true">
                        <img src="${ctx}/image/white_cart.png" alt="" width="24" height="24" />
                    </span>
                </article>
                <article class="vp-stat-card">
                    <div>
                        <p class="vp-stat-card__label">Pending Deliveries</p>
                        <p class="vp-stat-card__value">3</p>
                    </div>
                    <span class="vp-stat-card__icon vp-stat-card__icon--orange" aria-hidden="true">
                        <img src="${ctx}/image/truck.png" alt="" width="24" height="24" />
                    </span>
                </article>
                <article class="vp-stat-card">
                    <div>
                        <p class="vp-stat-card__label">Total Earnings</p>
                        <p class="vp-stat-card__value">Rs. 58.88</p>
                    </div>
                    <span class="vp-stat-card__icon vp-stat-card__icon--green" aria-hidden="true">
                        <img src="${ctx}/image/rupee.png" alt="" width="24" height="24" />
                    </span>
                </article>
            </section>

            <section class="vp-charts" aria-label="Analytics">
                <article class="vp-chart-card">
                    <h2>Revenue Trends</h2>
                    <div class="vp-chart-wrap">
                        <canvas id="revenueChart" aria-label="Revenue trends line chart"></canvas>
                    </div>
                </article>
                <article class="vp-chart-card">
                    <h2>Weekly Requests</h2>
                    <div class="vp-chart-wrap">
                        <canvas id="weeklyChart" aria-label="Weekly requests bar chart"></canvas>
                    </div>
                </article>
            </section>

            <section class="vp-orders-card" aria-labelledby="recent-orders-heading">
                <h2 id="recent-orders-heading">Recent Orders</h2>
                <div class="vp-table-wrap">
                    <table class="vp-table">
                        <thead>
                            <tr>
                                <th scope="col">Order ID</th>
                                <th scope="col">Customer</th>
                                <th scope="col">Date</th>
                                <th scope="col">Total</th>
                                <th scope="col">Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>ORD-001</td>
                                <td>John Doe</td>
                                <td>2026-05-15</td>
                                <td>Rs. 24.95</td>
                                <td><span class="vp-badge vp-badge--pending">Pending</span></td>
                            </tr>
                            <tr>
                                <td>ORD-002</td>
                                <td>Jane Smith</td>
                                <td>2026-05-16</td>
                                <td>Rs. 18.97</td>
                                <td><span class="vp-badge vp-badge--dispatched">Dispatched</span></td>
                            </tr>
                            <tr>
                                <td>ORD-003</td>
                                <td>Mike Johnson</td>
                                <td>2026-05-17</td>
                                <td>Rs. 31.95</td>
                                <td><span class="vp-badge vp-badge--dispatched">Dispatched</span></td>
                            </tr>
                            <tr>
                                <td>ORD-004</td>
                                <td>Sarah Williams</td>
                                <td>2026-05-17</td>
                                <td>Rs. 15.97</td>
                                <td><span class="vp-badge vp-badge--pending">Pending</span></td>
                            </tr>
                            <tr>
                                <td>ORD-005</td>
                                <td>David Brown</td>
                                <td>2026-05-18</td>
                                <td>Rs. 22.96</td>
                                <td><span class="vp-badge vp-badge--delivered">Delivered</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </section>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js" crossorigin="anonymous"></script>
<script src="${ctx}/js/farmer-dashboard.js"></script>
</body>
</html>
