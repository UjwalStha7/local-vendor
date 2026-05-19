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

            <section class="chart-container" aria-label="Weekly activity chart">
                <h3 class="chart-container__title">Weekly Vendor Applications</h3>
                <p class="chart-container__subtitle">New farmer applications submitted in the last 7 days</p>
                <div class="chart-wrapper">
                    <canvas id="weeklyChart" aria-label="Weekly vendor applications line chart"></canvas>
                </div>
            </section>
        </main>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js" crossorigin="anonymous"></script>
<script>
    const labels = [
        <c:forEach var="d" items="${dates}" varStatus="status">
            "<c:out value='${d}' />"<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    const data = [
        <c:forEach var="c" items="${counts}" varStatus="status">
            ${c}<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    const ctx = document.getElementById("weeklyChart");
    if (ctx && typeof Chart !== "undefined") {
        new Chart(ctx, {
            type: "line",
            data: {
                labels: labels,
                datasets: [{
                    label: "Vendor applications",
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
                    legend: {
                        display: true,
                        position: "top"
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            precision: 0
                        },
                        grid: {
                            color: "rgba(0, 0, 0, 0.06)"
                        }
                    },
                    x: {
                        grid: {
                            display: false
                        }
                    }
                }
            }
        });
    }
</script>
</body>
</html>
