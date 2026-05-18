<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:if test="${empty activeNav}"><c:set var="activeNav" value="dashboard" /></c:if>

<aside class="vp-sidebar" aria-label="Vendor navigation">
    <a class="vp-sidebar__brand" href="${previewMode ? ctx.concat('/farmerdshboard') : ctx.concat('/farmer/dashboard')}">
        <span class="vp-sidebar__logo" aria-hidden="true">
            <img src="${ctx}/image/box.png" alt="" width="22" height="22" />
        </span>
        <span>
            <p class="vp-sidebar__title">Vendor Portal</p>
            <p class="vp-sidebar__tagline">Manage your store</p>
        </span>
    </a>

    <nav class="vp-sidebar__nav" aria-label="Main menu">
        <c:choose>
            <c:when test="${previewMode}">
                <a class="vp-nav-item${activeNav eq 'dashboard' ? ' is-active' : ''}"
                   href="${ctx}/farmerdshboard"
                   ${activeNav eq 'dashboard' ? 'aria-current="page"' : ''}>
                    <img src="${ctx}/image/dashboard.png" alt="" width="20" height="20" />
                    <span>Dashboard</span>
                </a>
                <a class="vp-nav-item${activeNav eq 'product-management' ? ' is-active' : ''}"
                   href="${ctx}/farmerproductmanagement"
                   ${activeNav eq 'product-management' ? 'aria-current="page"' : ''}>
                    <img src="${ctx}/image/box.png" alt="" width="20" height="20" />
                    <span>Product Management</span>
                </a>
                <a class="vp-nav-item${activeNav eq 'orders' ? ' is-active' : ''}"
                   href="${ctx}/farmerorderspreview"
                   ${activeNav eq 'orders' ? 'aria-current="page"' : ''}>
                    <img src="${ctx}/image/gray_cart.png" alt="" width="20" height="20" />
                    <span>Order Management</span>
                </a>
                <a class="vp-nav-item${activeNav eq 'profile' ? ' is-active' : ''}"
                   href="${ctx}/farmerprofilepreview"
                   ${activeNav eq 'profile' ? 'aria-current="page"' : ''}>
                    <img src="${ctx}/image/user.png" alt="" width="20" height="20" />
                    <span>Vendor Profile</span>
                </a>
            </c:when>
            <c:otherwise>
                <a class="vp-nav-item${activeNav eq 'dashboard' ? ' is-active' : ''}"
                   href="${ctx}/farmer/dashboard"
                   ${activeNav eq 'dashboard' ? 'aria-current="page"' : ''}>
                    <img src="${ctx}/image/dashboard.png" alt="" width="20" height="20" />
                    <span>Dashboard</span>
                </a>
                <a class="vp-nav-item${activeNav eq 'product-management' ? ' is-active' : ''}"
                   href="${ctx}/farmer/product-management"
                   ${activeNav eq 'product-management' ? 'aria-current="page"' : ''}>
                    <img src="${ctx}/image/box.png" alt="" width="20" height="20" />
                    <span>Product Management</span>
                </a>
                <a class="vp-nav-item${activeNav eq 'orders' ? ' is-active' : ''}"
                   href="${ctx}/farmer/orders"
                   ${activeNav eq 'orders' ? 'aria-current="page"' : ''}>
                    <img src="${ctx}/image/gray_cart.png" alt="" width="20" height="20" />
                    <span>Order Management</span>
                </a>
                <a class="vp-nav-item${activeNav eq 'profile' ? ' is-active' : ''}"
                   href="${ctx}/farmer/profile"
                   ${activeNav eq 'profile' ? 'aria-current="page"' : ''}>
                    <img src="${ctx}/image/user.png" alt="" width="20" height="20" />
                    <span>Vendor Profile</span>
                </a>
            </c:otherwise>
        </c:choose>
    </nav>

    <div class="vp-sidebar__footer">
        <c:choose>
            <c:when test="${previewMode}">
                <a class="vp-nav-item vp-nav-item--logout" href="${ctx}/login">
                    <img src="${ctx}/image/logout.png" alt="" width="20" height="20" />
                    <span>Sign in</span>
                </a>
            </c:when>
            <c:otherwise>
                <a class="vp-nav-item vp-nav-item--logout" href="${ctx}/farmer/logout">
                    <img src="${ctx}/image/logout.png" alt="" width="20" height="20" />
                    <span>Logout</span>
                </a>
            </c:otherwise>
        </c:choose>
    </div>
</aside>
