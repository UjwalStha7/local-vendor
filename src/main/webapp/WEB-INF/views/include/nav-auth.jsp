<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- Shows Login when anonymous; otherwise account menu with name, role, email, logout. --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="u" value="${sessionScope.user}" />
<c:choose>
  <c:when test="${empty u}">
    <a class="nav-login-btn" href="${ctx}/login">Login</a>
  </c:when>
  <c:otherwise>
    <c:set var="displayName" value="${not empty u.username ? u.username : u.email}" />
    <c:set var="roleKey" value="${not empty u.role ? u.role : sessionScope.role}" />
    <c:choose>
      <c:when test="${fn:toLowerCase(roleKey) eq 'admin'}">
        <c:set var="roleLabel" value="Administrator" />
      </c:when>
      <c:when test="${fn:toLowerCase(roleKey) eq 'vendor'}">
        <c:set var="roleLabel" value="Farmer" />
      </c:when>
      <c:otherwise>
        <c:set var="roleLabel" value="Customer" />
      </c:otherwise>
    </c:choose>
    <c:set var="initial" value="${fn:toUpperCase(fn:substring(displayName, 0, 1))}" />
    <details class="nav-profile">
      <summary class="nav-profile__summary" aria-label="Account menu, ${fn:escapeXml(displayName)}">
        <span class="nav-profile__avatar" aria-hidden="true"><c:out value="${initial}" /></span>
        <span class="nav-profile__name-short"><c:out value="${displayName}" /></span>
        <span class="nav-profile__chevron" aria-hidden="true"></span>
      </summary>
      <div class="nav-profile__panel" role="region" aria-label="Account">
        <div class="nav-profile__row">
          <span class="nav-profile__key">Name</span>
          <span class="nav-profile__val"><c:out value="${displayName}" /></span>
        </div>
        <div class="nav-profile__row">
          <span class="nav-profile__key">Role</span>
          <span class="nav-profile__val"><c:out value="${roleLabel}" /></span>
        </div>
        <div class="nav-profile__row">
          <span class="nav-profile__key">Email</span>
          <span class="nav-profile__val nav-profile__val--email"><c:out value="${u.email}" /></span>
        </div>
        <a class="nav-profile__logout" href="${ctx}/logout">Log out</a>
      </div>
    </details>
  </c:otherwise>
</c:choose>
