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
        <div class="vp-profile" title="${not empty sessionScope.user ? sessionScope.user.email : ''}">
            <img class="vp-profile__avatar"
                 src="${empty storeLogo ? ctx.concat('/image/fresh_apple.png') : storeLogo}"
                 alt=""
                 width="42"
                 height="42" />
            <div class="vp-profile__text">
                <p class="vp-profile__name"><c:out value="${storeName}" /></p>
                <p class="vp-profile__role">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <span class="vp-profile__email"><c:out value="${sessionScope.user.email}" /></span>
                        </c:when>
                        <c:otherwise>Vendor</c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>
    </div>
</header>
