<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:if test="${empty storeName}"><c:set var="storeName" value="FreshHarvest Farms" /></c:if>

<header class="vp-topbar">
    <c:if test="${topbarShowSearch}">
        <form class="vp-search" role="search" method="get" action="${topbarSearchAction}">
            <img src="${ctx}/image/search.png" alt="" width="18" height="18" aria-hidden="true" />
            <input type="search" name="q" placeholder="${empty topbarSearchPlaceholder ? 'Search...' : topbarSearchPlaceholder}"
                   value="<c:out value='${topbarSearchValue}' />"
                   aria-label="Search" />
        </form>
    </c:if>

    <div class="vp-topbar__actions${topbarShowSearch ? '' : ' vp-topbar__actions--end'}">
        <div class="vp-profile">
            <img class="vp-profile__avatar"
                 src="${empty storeLogo ? ctx.concat('/image/fresh_apple.png') : storeLogo}"
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
