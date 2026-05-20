<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- param.current: home | product | about | contact --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="nav" value="${param.current}" />
<header class="site-header" id="top">
  <div class="header-inner">
    <a class="logo" href="${ctx}/">Krishak</a>
    <nav class="main-nav" aria-label="Primary">
      <a href="${ctx}/"
         <c:if test="${nav == 'home'}">class="nav-current" aria-current="page"</c:if>>Home</a>
      <a href="${ctx}/product"
         <c:if test="${nav == 'product'}">class="nav-current" aria-current="page"</c:if>>Product</a>
      <a href="${ctx}/about"
         <c:if test="${nav == 'about'}">class="nav-current" aria-current="page"</c:if>>About Us</a>
      <a href="${ctx}/contact"
         <c:if test="${nav == 'contact'}">class="nav-current" aria-current="page"</c:if>>Contact</a>
    </nav>
    <div class="header-actions">
      <jsp:include page="/WEB-INF/views/include/nav-auth.jsp" />
      <a class="cart-link" href="${ctx}/cart" aria-label="Shopping cart">
        <img src="${ctx}/image/cart.png" alt="" width="24" height="24" />
        <span id="cart-badge" class="cart-badge" aria-live="polite">0</span>
      </a>
    </div>
  </div>
</header>
<script src="${ctx}/cart-badge.js"></script>
