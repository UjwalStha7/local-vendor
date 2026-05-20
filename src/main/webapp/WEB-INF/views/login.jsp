<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Login</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />
</head>
<body class="login-body">

    <main class="auth-page">
        <section class="auth-card" aria-labelledby="login-heading">
            <div class="logo-wrap">
                <div class="logo-circle" aria-hidden="true">
                    <img src="${pageContext.request.contextPath}/image/white_lock.png" alt="" />
                </div>
            </div>

            <div class="welcome">
                <h1 id="login-heading">Welcome back</h1>
                <p>Customers: use your registered email (e.g. name@gmail.com). Farmers: use your @krishak.np address.</p>
            </div>

            <nav class="switcher" aria-label="Authentication pages">
                <a class="switch-link active" href="${pageContext.request.contextPath}/login">Login</a>
                <a class="switch-link" href="${pageContext.request.contextPath}/register">Register</a>
            </nav>

            <form class="auth-form" action="${pageContext.request.contextPath}/login" method="post">
                <c:if test="${not empty error}">
                    <p class="auth-error"><c:out value="${error}" /></p>
                </c:if>

                <div class="form-group">
                    <label for="login-email">Email</label>
                    <div class="input-wrap">
                        <span class="input-icon" aria-hidden="true">
                            <img class="icon-img" src="${pageContext.request.contextPath}/image/email.png" alt="" />
                        </span>
                        <input id="login-email" type="email" name="email" placeholder="name@gmail.com or you@krishak.np"
                               value="<c:out value='${email}' default='${param.email}'/>" required />
                    </div>
                </div>

                <div class="form-group">
                    <label for="login-password">Password</label>
                    <div class="input-wrap">
                        <span class="input-icon" aria-hidden="true">
                            <img class="icon-img" src="${pageContext.request.contextPath}/image/lock.png" alt="" />
                        </span>
                        <input id="login-password" type="password" name="password"
                               placeholder="Enter your password" required />
                    </div>
                </div>

                <button class="submit-btn" type="submit">Sign In</button>
            </form>

            <p class="auth-switch-text">
                Don't have an account?
                <a href="${pageContext.request.contextPath}/register">Register</a>
            </p>

        </section>
    </main>
</body>
</html>
