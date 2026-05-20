<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- param.current: home | product | about | contact --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="nav" value="${param.current}" />
<c:set var="sessionUser" value="${sessionScope.user}" />
<c:set var="sessionRole" value="${sessionScope.role}" />
<c:set var="hasVendorProfile" value="${sessionScope.hasVendorProfile}" />
<c:set var="isCustomerSession" value="${sessionRole == 'customer' && not empty sessionUser}" />
<c:set var="isVendorSession" value="${sessionRole == 'vendor' && not empty sessionUser}" />
<c:set var="showUserMenu" value="${isCustomerSession || isVendorSession}" />
<c:if test="${showUserMenu}">
  <c:choose>
    <c:when test="${not empty sessionUser.username}">
      <c:set var="customerDisplayName" value="${sessionUser.username}" />
    </c:when>
    <c:otherwise>
      <c:set var="customerDisplayName" value="${sessionUser.email}" />
    </c:otherwise>
  </c:choose>
</c:if>
<header class="site-header" id="top">
  <motion class="header-inner">