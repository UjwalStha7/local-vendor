<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Top bar: page title and subtitle --%>
<%
    String pageTitle = (String) request.getAttribute("pageTitle");
    String pageSubtitle = (String) request.getAttribute("pageSubtitle");
    if (pageTitle == null) {
        pageTitle = "Admin";
    }
    if (pageSubtitle == null) {
        pageSubtitle = "";
    }
%>
<header class="admin-topbar">
    <div class="admin-topbar__titles">
        <h1 class="admin-topbar__title"><%= pageTitle %></h1>
        <p class="admin-topbar__subtitle"><%= pageSubtitle %></p>
    </div>
</header>
