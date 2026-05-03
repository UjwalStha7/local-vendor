<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Top bar: titles + user + logout --%>
<%
    String ctx = request.getContextPath();
    String pageTitle = (String) request.getAttribute("pageTitle");
    String pageSubtitle = (String) request.getAttribute("pageSubtitle");
    String adminDisplayName = (String) request.getAttribute("adminDisplayName");
    if (pageTitle == null) {
        pageTitle = "Admin";
    }
    if (pageSubtitle == null) {
        pageSubtitle = "";
    }
    if (adminDisplayName == null) {
        adminDisplayName = "Admin";
    }
%>
<header class="admin-topbar">
    <div class="admin-topbar__titles">
        <h1 class="admin-topbar__title"><%= pageTitle %></h1>
        <p class="admin-topbar__subtitle"><%= pageSubtitle %></p>
    </div>
    <div class="admin-topbar__actions">
        <span class="admin-topbar__user" aria-label="Signed in user"><%= adminDisplayName %></span>
        <a class="admin-topbar__logout" href="<%= ctx %>/admin?section=signout" aria-label="Sign out">
            <img src="<%= ctx %>/image/logout.png" alt="" width="20" height="20" />
        </a>
    </div>
</header>
