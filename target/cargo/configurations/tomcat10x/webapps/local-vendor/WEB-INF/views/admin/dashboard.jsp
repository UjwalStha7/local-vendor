<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Legacy path: redirect to servlet-driven dashboard --%>
<%
    response.sendRedirect(request.getContextPath() + "/admin?section=dashboard");
%>
