<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Expects: activeNav (String), contextPath via request --%>
<%
    String ctx = request.getContextPath();
    String nav = (String) request.getAttribute("activeNav");
    if (nav == null) {
        nav = "dashboard";
    }
%>
<aside class="admin-sidebar" aria-label="Admin navigation">
    <div class="admin-sidebar__brand">
        <p class="admin-sidebar__brand-name">Krishak</p>
        <p class="admin-sidebar__brand-sub">Admin Panel</p>
    </div>

    <nav class="admin-sidebar__nav">
        <a class="admin-nav-item<%= "dashboard".equals(nav) ? " is-active" : "" %>"
           href="<%= ctx %>/admin?section=dashboard">
            <span class="admin-nav-item__icon" aria-hidden="true">
                <img src="<%= ctx %>/image/dashboard.png" alt="" width="20" height="20" />
            </span>
            <span class="admin-nav-item__label">Dashboard</span>
        </a>
        <a class="admin-nav-item<%= "requests".equals(nav) ? " is-active" : "" %>"
           href="<%= ctx %>/admin?section=requests">
            <span class="admin-nav-item__icon" aria-hidden="true">
                <img src="<%= ctx %>/image/vendor.png" alt="" width="20" height="20" />
            </span>
            <span class="admin-nav-item__label">Vendor Requests</span>
        </a>
        <a class="admin-nav-item<%= "add-vendor".equals(nav) ? " is-active" : "" %>"
           href="<%= ctx %>/admin?section=add-vendor">
            <span class="admin-nav-item__icon" aria-hidden="true">
                <img src="<%= ctx %>/image/vendor.png" alt="" width="20" height="20" />
            </span>
            <span class="admin-nav-item__label">Add new Vendor</span>
        </a>
        <a class="admin-nav-item<%= "accounts".equals(nav) ? " is-active" : "" %>"
           href="<%= ctx %>/admin?section=accounts">
            <span class="admin-nav-item__icon" aria-hidden="true">
                <img src="<%= ctx %>/image/vendor.png" alt="" width="20" height="20" />
            </span>
            <span class="admin-nav-item__label">Vendor Accounts</span>
        </a>
        <a class="admin-nav-item<%= "moderation".equals(nav) ? " is-active" : "" %>"
           href="<%= ctx %>/admin?section=moderation">
            <span class="admin-nav-item__icon" aria-hidden="true">
                <img src="<%= ctx %>/image/box.png" alt="" width="20" height="20" />
            </span>
            <span class="admin-nav-item__label">Product Moderation</span>
        </a>
    </nav>

    <div class="admin-sidebar__footer">
        <a class="admin-signout" href="<%= ctx %>/admin?section=signout">
            <span class="admin-signout__icon" aria-hidden="true">
                <img src="<%= ctx %>/image/logout.png" alt="" width="18" height="18" />
            </span>
            <span>Sign Out</span>
        </a>
    </div>
</aside>
