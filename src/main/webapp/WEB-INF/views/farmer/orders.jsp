<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Order Management" />
<c:set var="activeNav" value="orders" scope="request" />
<jsp:include page="vendor-head.jsp" />

<c:if test="${previewMode}">
    <p class="vp-preview-banner" role="status">
        Preview mode — not logged in. Sign in as a vendor to manage orders at
        <a href="${ctx}/farmer/orders">/farmer/orders</a>.
    </p>
</c:if>

<header class="vp-page-head vp-page-head--orders">
    <div>
        <h1>Order Management</h1>
        <p>View and manage all incoming orders</p>
    </div>
    <form class="vp-orders-filter" method="get" action="${previewMode ? ctx.concat('/farmerorderspreview') : ctx.concat('/farmer/orders')}">
        <c:if test="${not empty searchQuery}">
            <input type="hidden" name="q" value="${searchQuery}" />
        </c:if>
        <label class="vp-sr-only" for="orderStatusFilter">Filter orders by status</label>
        <select id="orderStatusFilter" name="filter" class="vp-filter-select" onchange="this.form.submit()">
            <option value="all" ${orderFilter eq 'all' ? 'selected' : ''}>All Orders</option>
            <option value="pending" ${orderFilter eq 'pending' ? 'selected' : ''}>Pending</option>
            <option value="dispatched" ${orderFilter eq 'dispatched' ? 'selected' : ''}>Dispatched</option>
            <option value="delivered" ${orderFilter eq 'delivered' ? 'selected' : ''}>Delivered</option>
        </select>
    </form>
</header>

<section class="vp-order-stats" aria-label="Order summary">
    <article class="vp-order-stat vp-order-stat--total">
        <p class="vp-order-stat__label">Total Orders</p>
        <p class="vp-order-stat__value">${statTotal}</p>
    </article>
    <article class="vp-order-stat vp-order-stat--pending">
        <p class="vp-order-stat__label">Pending</p>
        <p class="vp-order-stat__value">${statPending}</p>
    </article>
    <article class="vp-order-stat vp-order-stat--dispatched">
        <p class="vp-order-stat__label">Dispatched</p>
        <p class="vp-order-stat__value">${statDispatched}</p>
    </article>
    <article class="vp-order-stat vp-order-stat--delivered">
        <p class="vp-order-stat__label">Delivered</p>
        <p class="vp-order-stat__value">${statDelivered}</p>
    </article>
</section>

<section class="vp-orders-card vp-orders-card--full" aria-label="Orders list">
    <div class="vp-table-wrap">
        <table class="vp-table vp-orders-table" id="ordersTable">
            <thead>
                <tr>
                    <th scope="col">Order ID</th>
                    <th scope="col">Customer</th>
                    <th scope="col">Date</th>
                    <th scope="col">Total</th>
                    <th scope="col">Status</th>
                    <th scope="col">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty orders}">
                        <tr>
                            <td colspan="6" class="vp-table-empty">No orders match your filters.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="o" items="${orders}">
                            <tr data-order-row data-status="${o.statusKey}"
                                data-search="${o.orderId} ${o.customerName} ${o.phone}">
                                <td class="vp-order-id">${o.orderId}</td>
                                <td>
                                    <span class="vp-customer-name">${o.customerName}</span>
                                    <span class="vp-customer-phone">${o.phone}</span>
                                </td>
                                <td>${o.orderDate}</td>
                                <td class="vp-order-total">Rs. <fmt:formatNumber value="${o.total}" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${previewMode}">
                                            <span class="vp-status-pill vp-status-pill--${o.statusKey}">${o.status}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <form method="post" action="${ctx}/farmer/orders" class="vp-status-form">
                                                <input type="hidden" name="orderId" value="${o.orderId}" />
                                                <input type="hidden" name="filter" value="${orderFilter}" />
                                                <label class="vp-sr-only" for="status-${o.orderId}">Status for ${o.orderId}</label>
                                                <select id="status-${o.orderId}" name="status"
                                                        class="vp-status-select vp-status-select--${o.statusKey}"
                                                        onchange="this.form.submit()">
                                                    <option value="Pending" ${o.status eq 'Pending' ? 'selected' : ''}>Pending</option>
                                                    <option value="Dispatched" ${o.status eq 'Dispatched' ? 'selected' : ''}>Dispatched</option>
                                                    <option value="Delivered" ${o.status eq 'Delivered' ? 'selected' : ''}>Delivered</option>
                                                </select>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <button type="button" class="vp-icon-btn vp-view-btn"
                                            aria-label="View order ${o.orderId}"
                                            data-order-id="${o.orderId}">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                                            <path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z"/>
                                            <circle cx="12" cy="12" r="3"/>
                                        </svg>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</section>

<script src="${ctx}/js/farmer-orders.js"></script>
<jsp:include page="vendor-foot.jsp" />
