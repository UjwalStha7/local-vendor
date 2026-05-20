<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Login / register header (no cart) --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<header class="site-header">
  <div class="header-inner">
    <a class="logo" href="${ctx}/">Krishak</a>
    <nav class="main-nav" aria-label="Primary">
      <a href="${ctx}/">Home</a>
      <a href="${ctx}/product">Product</a>
      <a href="${ctx}/about">About Us</a>
      <a href="${ctx}/contact">Contact</a>
    </nav>
    <div class="header-actions">
      <jsp:include page="/WEB-INF/views/include/nav-auth.jsp" />
    </div>
  </div>
</header>
