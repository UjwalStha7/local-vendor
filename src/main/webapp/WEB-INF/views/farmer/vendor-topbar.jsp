<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:if test="${empty storeName}"><c:set var="storeName" value="FreshHarvest Farms" /></c:if>

<header class="vp-topbar">
    <form class="vp-search" role="search" action="#" method="get" onsubmit="return false;">
        <img src="${ctx}/image/search.png" alt="" width="18" height="18" aria-hidden="true" />
        <input type="search" name="q" placeholder="Search..." aria-label="Search" />
    </form>

    <div class="vp-topbar__actions">
        <button type="button" class="vp-notify" aria-label="Notifications">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
                <path d="M13.73 21a2 2 0 0 1-3.46 0" />
            </svg>
            <span class="vp-notify__dot" aria-hidden="true"></span>
        </button>

        <div class="vp-profile">
            <img class="vp-profile__avatar"
                 src="${ctx}/image/fresh_apple.png"
                 alt=""
                 width="42"
                 height="42" />
            <div class="vp-profile__text">
                <p class="vp-profile__name"><c:out value="${storeName}" /></p>
                <p class="vp-profile__role">Vendor</p>
            </div>
        </div>
    </div>
</header>
