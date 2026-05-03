<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Learning Log — Register</title>
    </head>

<body>
<div>


<form action="${pageContext.request.contextPath}/register" method="post">
    <h1> Register</h1>
<%--    Displaing error message to user--%>
    <c:if test="${not empty error}">
        <p class="error"><c:out value="${error}" /></p>
    </c:if>

    <input type="text" name="username" placeholder="username"
    value="<c:out value="${param.username}" default=''/>" required />
    <input type="email" name="email" placeholder="Email"
    value="<c:out value="${param.email}" default=''/>" required />
    <input type="password" name="password" placeholder="Password" required />
    <input type="password" name="confirmed_password" placeholder="Confirm Password" required />
    <input type="number" name="phone" placeholder="Phone" required />

    <button type="submit"> <h3> Register </h3></button>
</form>
</div>
</body>
