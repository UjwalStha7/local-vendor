<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Local Vendor — Login</title>
</head>

<body>
<div>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <h1> Log-In </h1>
        <c:if test="${not empty error}">
        <p class="error"><c:out value="${error}" /></p>
        </c:if>
        <input type="email" name="email" placeholder="email"
               value="<c:out value="${param.email}" default=''/>" required />
        <input type="password" name="password" placeholder="password" />
        <button type="submit"><h2>Log-in</h2></button>

        <p class="link">Don't have an account?
            <a href="${pageContext.request.contextPath}/register">Register</a>
        </p>
        </form>
    </div>
</body>


