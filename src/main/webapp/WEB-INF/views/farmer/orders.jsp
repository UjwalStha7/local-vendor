<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Order Management" />
<c:set var="activeNav" value="orders" scope="request" />
<jsp:include page="vendor-head.jsp" />

<header class="vp-page-head">
    <h1>Order Management</h1>
    <p>Track and fulfill customer orders for your farm.</p>
</header>

<section class="vp-placeholder" aria-label="Order management">
    <h2>Coming soon</h2>
    <p>Full order tracking and status updates will be available here.</p>
</section>

<jsp:include page="vendor-foot.jsp" />
