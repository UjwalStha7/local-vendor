<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
<c:if test="${empty storeName}"><c:set var="storeName" value="Your store" /></c:if>

<div class="vp-shell">
    <jsp:include page="vendor-sidebar.jsp" />

    <div class="vp-main">
        <jsp:include page="vendor-topbar.jsp" />

        <main class="vp-content">
            <header class="vp-page-head">
                <h1>Dashboard Overview</h1>
                <p>Welcome back! Here's what's happening with your store today.</p>
            </header>

            <section class="vp-stats" aria-label="Store metrics">
                <article class="vp-stat-card">
                    <div>
                        <p class="vp-stat-card__label">Total Products Listed</p>
                        <p class="vp-stat-card__value">${statProductsListed}</p>
                    </div>
                    <span class="vp-stat-card__icon vp-stat-card__icon--blue" aria-hidden="true">
                        <img src="${ctx}/image/box.png" alt="" width="24" height="24" />
                    </span>
                </article>
                <article class="vp-stat-card">
                    <div>
                        <p class="vp-stat-card__label">Active Orders</p>
                        <p class="vp-stat-card__value">${statActiveOrders}</p>
                    </div>
                    <span class="vp-stat-card__icon vp-stat-card__icon--purple" aria-hidden="true">
                        <img src="${ctx}/image/white_cart.png" alt="" width="24" height="24" />
                    </span>
                </article>
                <article class="vp-stat-card">
                    <div>
                        <p class="vp-stat-card__label">Pending Deliveries</p>
                        <p class="vp-stat-card__value">${statPendingDeliveries}</p>
                    </div>
                    <span class="vp-stat-card__icon vp-stat-card__icon--orange" aria-hidden="true">
                        <img src="${ctx}/image/truck.png" alt="" width="24" height="24" />
                    </span>
                </article>
                <article class="vp-stat-card">
                    <div>
                        <p class="vp-stat-card__label">Total Earnings</p>
                        <p class="vp-stat-card__value">Rs. <fmt:formatNumber value="${statTotalEarnings}" minFractionDigits="2" maxFractionDigits="2" /></p>
                    </div>
                    <span class="vp-stat-card__icon vp-stat-card__icon--green" aria-hidden="true">
                        <img src="${ctx}/image/rupee.png" alt="" width="24" height="24" />
                    </span>
                </article>
            </section>

            <section class="vp-chart-container" aria-label="Weekly orders chart">
                <h2 class="vp-chart-container__title">Weekly Orders</h2>
                <p class="vp-chart-container__subtitle">Orders containing your products in the last 7 days</p>
                <div class="vp-chart-wrapper">
                    <canvas id="weeklyOrdersChart" aria-label="Weekly orders line chart"></canvas>
                </div>
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
                            <c:forEach var="row" items="${recentOrders}">
                                <tr>
                                    <td><c:out value="${row.orderId}" /></td>
                                    <td><c:out value="${row.customerName}" /></td>
                                    <td><c:out value="${row.orderDate}" /></td>
                                    <td>Rs. <fmt:formatNumber value="${row.total}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${row.statusKey eq 'delivered'}">
                                                <span class="vp-badge vp-badge--delivered">Delivered</span>
                                            </c:when>
                                            <c:when test="${row.statusKey eq 'dispatched'}">
                                                <span class="vp-badge vp-badge--dispatched">Dispatched</span>
                                            </c:when>
                                            <c:when test="${row.statusKey eq 'cancelled'}">
                                                <span class="vp-badge vp-badge--cancelled">Cancelled</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="vp-badge vp-badge--pending">Pending</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty recentOrders}">
                                <tr>
                                    <td colspan="5" class="vp-table-empty">No orders yet.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js" crossorigin="anonymous"></script>
<script>
    const labels = [
        <c:forEach var="d" items="${chartDates}" varStatus="status">
            "<c:out value='${d}' />"<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];
    const data = [
        <c:forEach var="c" items="${chartCounts}" varStatus="status">
            ${c}<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];
    const chartEl = document.getElementById("weeklyOrdersChart");
    if (chartEl && typeof Chart !== "undefined") {
        new Chart(chartEl, {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: "Orders",
                    data: data,
                    borderColor: "#1b4332",
                    backgroundColor: "rgba(45, 106, 79, 0.15)",
                    borderWidth: 2,
                    tension: 0.3,
                    fill: true,
                    pointBackgroundColor: "#2d6a4f",
                    pointBorderColor: "#ffffff",
                    pointBorderWidth: 2,
                    pointRadius: 4,
                    pointHoverRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: true, position: "top" }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { precision: 0 },
                        grid: { color: "rgba(0, 0, 0, 0.06)" }
                    },
                    x: { grid: { display: false } }
                }
            }
        });
    }
</script>
</body>
</html>
