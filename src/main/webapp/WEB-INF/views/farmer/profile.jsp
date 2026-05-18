<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Vendor Profile" />
<c:set var="activeNav" value="profile" scope="request" />
<jsp:include page="vendor-head.jsp" />

<header class="vp-page-head">
    <h1>Vendor Profile</h1>
    <p>View and manage your store profile information.</p>
</header>

<section class="vp-orders-card vp-profile-card" aria-label="Profile details">
    <div class="vp-profile-detail">
        <img class="vp-profile__avatar vp-profile__avatar--lg"
             src="${ctx}/image/fresh_apple.png"
             alt=""
             width="80"
             height="80" />
        <div>
            <h2 class="vp-profile-detail__name"><c:out value="${storeName}" /></h2>
            <p class="vp-profile-detail__role">Vendor</p>
        </div>
    </div>
    <dl class="vp-profile-fields">
        <div>
            <dt>Email</dt>
            <dd><c:out value="${vendorEmail}" default="—" /></dd>
        </div>
        <div>
            <dt>Phone</dt>
            <dd><c:out value="${vendorPhone}" default="—" /></dd>
        </div>
        <div>
            <dt>Store</dt>
            <dd><c:out value="${storeName}" /></dd>
        </div>
    </dl>
</section>

<jsp:include page="vendor-foot.jsp" />
