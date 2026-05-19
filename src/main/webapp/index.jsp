<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
    /* Root URL must show the public home; do not map a servlet to "/" — that breaks static CSS/JS. */
    request.getRequestDispatcher("/WEB-INF/views/customer/home.jsp").forward(request, response);
%>
