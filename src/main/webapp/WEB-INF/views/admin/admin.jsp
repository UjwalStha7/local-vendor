<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
<div class="admin-app">
    <jsp:include page="sidebar.jsp" />

    <div class="admin-main">
        <jsp:include page="header.jsp" />

        <main class="admin-content">
            <section class="admin-stats" aria-label="Key metrics">
                <article class="stat-card">
                    <div class="stat-card__icon stat-card__icon--orange">
                        <img src="${pageContext.request.contextPath}/image/clock.png" alt="" width="22" height="22" />
                    </div>
                    <div class="stat-card__body">
                        <p class="stat-card__label">Pending Requests</p>
                        <p class="stat-card__value">${statPendingRequests}</p>
                    </div>
                </article>
                <article class="stat-card">
                    <div class="stat-card__icon stat-card__icon--green">
                        <img src="${pageContext.request.contextPath}/image/vendor.png" alt="" width="22" height="22" />
                    </div>
                    <div class="stat-card__body">
                        <p class="stat-card__label">Active Vendors</p>
                        <p class="stat-card__value">${statActiveVendors}</p>
                    </div>
                </article>
                <article class="stat-card">
                    <div class="stat-card__icon stat-card__icon--blue">
                        <img src="${pageContext.request.contextPath}/image/box.png" alt="" width="22" height="22" />
                    </div>
                    <div class="stat-card__body">
                        <p class="stat-card__label">Products Listed</p>
                        <p class="stat-card__value">${statProductsListed}</p>
                    </div>
                </article>
                <article class="stat-card">
                    <div class="stat-card__icon stat-card__icon--purple">
                        <img src="${pageContext.request.contextPath}/image/white_cart.png" alt="" width="22" height="22" />
                    </div>
                    <div class="stat-card__body">
                        <p class="stat-card__label">Total Orders</p>
                        <p class="stat-card__value">${statTotalOrders}</p>
                    </div>
                </article>
            </section>

            <section class="admin-charts" aria-label="Charts">
                <article class="chart-card">
                    <h2 class="chart-card__title">Revenue Trends</h2>
                    <div class="chart-card__plot chart-card__plot--revenue" role="img" aria-label="Revenue area chart Jan to May">
                        <div class="revenue-chart">
                            <div class="revenue-chart__y">
                                <span>80k</span>
                                <span>60k</span>
                                <span>40k</span>
                                <span>20k</span>
                                <span>0</span>
                            </div>
                            <div class="revenue-chart__area">
                                <svg viewBox="0 0 400 160" preserveAspectRatio="none" aria-hidden="true">
                                    <defs>
                                        <linearGradient id="revFill" x1="0" y1="0" x2="0" y2="1">
                                            <stop offset="0%" stop-color="#2d6a4f" stop-opacity="0.35" />
                                            <stop offset="100%" stop-color="#2d6a4f" stop-opacity="0.02" />
                                        </linearGradient>
                                    </defs>
                                    <path fill="url(#revFill)" d="M0,120 L40,100 L100,110 L160,70 L220,85 L280,45 L340,55 L400,40 L400,160 L0,160 Z" />
                                    <path fill="none" stroke="#1b4332" stroke-width="2.5"
                                          d="M0,120 L40,100 L100,110 L160,70 L220,85 L280,45 L340,55 L400,40" />
                                </svg>
                                <div class="revenue-chart__x">
                                    <span>Jan</span>
                                    <span>Feb</span>
                                    <span>Mar</span>
                                    <span>Apr</span>
                                    <span>May</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </article>

                <article class="chart-card">
                    <h2 class="chart-card__title">Vendor Requests (This Week)</h2>
                    <div class="chart-card__plot" role="img" aria-label="Weekly vendor requests bar chart">
                        <div style="height: 300px; position: relative;">
                            <canvas id="vendorRequestChart"></canvas>
                        </div>
                    </div>
                </article>
            </section>
        </main>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    const vendorRequestLabels = [
        <c:forEach var="label" items="${vendorRequestLabels}" varStatus="status">
            "${label}"<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    const vendorRequestData = [
        <c:forEach var="value" items="${vendorRequestData}" varStatus="status">
            ${value}<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    const vendorRequestCtx = document.getElementById("vendorRequestChart");
    if (vendorRequestCtx) {
        new Chart(vendorRequestCtx, {
            type: "bar",
            data: {
                labels: vendorRequestLabels,
                datasets: [{
                    label: "Vendor Requests",
                    data: vendorRequestData,
                    backgroundColor: "rgba(45, 106, 79, 0.55)",
                    borderColor: "rgba(27, 67, 50, 1)",
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
</script>
</body>
</html>
