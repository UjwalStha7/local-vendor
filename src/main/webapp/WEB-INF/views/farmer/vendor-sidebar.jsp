<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:if test="${empty activeNav}"><c:set var="activeNav" value="dashboard" /></c:if>

<aside class="vp-sidebar" aria-label="Vendor navigation">
    <div class="vp-sidebar__brand">
        <p class="vp-sidebar__brand-name">Krishak</p>
        <p class="vp-sidebar__brand-sub">Vendor Panel</p>
    </div>

    <nav class="vp-sidebar__nav" aria-label="Main menu">
        <a class="vp-nav-item${activeNav eq 'dashboard' ? ' is-active' : ''}"
           href="${ctx}/farmer/dashboard"
           ${activeNav eq 'dashboard' ? 'aria-current="page"' : ''}>
            <span class="vp-nav-item__icon" aria-hidden="true">
                <img src="${ctx}/image/dashboard.png" alt="" width="20" height="20" />
            </span>
            <span class="vp-nav-item__label">Dashboard</span>
        </a>
        <a class="vp-nav-item${activeNav eq 'product-management' ? ' is-active' : ''}"
           href="${ctx}/farmer/product-management"
           ${activeNav eq 'product-management' ? 'aria-current="page"' : ''}>
            <span class="vp-nav-item__icon" aria-hidden="true">
                <img src="${ctx}/image/box.png" alt="" width="20" height="20" />
            </span>
            <span class="vp-nav-item__label">Product Management</span>
        </a>
        <a class="vp-nav-item${activeNav eq 'orders' ? ' is-active' : ''}"
           href="${ctx}/farmer/orders"
           ${activeNav eq 'orders' ? 'aria-current="page"' : ''}>
            <span class="vp-nav-item__icon" aria-hidden="true">
                <img src="${ctx}/image/gray_cart.png" alt="" width="20" height="20" />
            </span>
            <span class="vp-nav-item__label">Order Management</span>
        </a>
        <a class="vp-nav-item${activeNav eq 'profile' ? ' is-active' : ''}"
           href="${ctx}/farmer/profile"
           ${activeNav eq 'profile' ? 'aria-current="page"' : ''}>
            <span class="vp-nav-item__icon" aria-hidden="true">
                <img src="${ctx}/image/user.png" alt="" width="20" height="20" />
            </span>
            <span class="vp-nav-item__label">Vendor Profile</span>
        </a>
    </nav>

    <div class="vp-sidebar__footer">
        <a class="vp-signout" href="${ctx}/farmer/logout">
            <span class="vp-signout__icon" aria-hidden="true">
                <img src="${ctx}/image/logout.png" alt="" width="18" height="18" />
            </span>
            <span>Sign Out</span>
        </a>
    </div>
</aside>
