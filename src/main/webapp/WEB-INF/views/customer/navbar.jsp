<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- param.current: home | shop | about | contact --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="nav" value="${param.current}" />
<header class="site-header" id="top">
  <div class="header-inner">
    <a class="logo" href="${ctx}/customer/home">Krishak</a>
    <nav class="main-nav" aria-label="Primary">
      <a href="${ctx}/customer/home"
         <c:if test="${nav == 'home'}">class="nav-current" aria-current="page"</c:if>>Home</a>
      <a href="${ctx}/shop"
         <c:if test="${nav == 'shop'}">class="nav-current" aria-current="page"</c:if>>Product</a>
      <a href="${ctx}/about"
         <c:if test="${nav == 'about'}">class="nav-current" aria-current="page"</c:if>>About Us</a>
      <a href="${ctx}/contact"
         <c:if test="${nav == 'contact'}">class="nav-current" aria-current="page"</c:if>>Contact</a>
    </nav>
    <a class="cart-link" href="${ctx}/cart.html" aria-label="Shopping cart">
      <img src="${ctx}/image/cart.png" alt="" width="24" height="24" />
      <span id="cart-badge" class="cart-badge" aria-live="polite">0</span>
    </a>
  </div>
</header>
