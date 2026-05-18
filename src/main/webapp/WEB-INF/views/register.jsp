<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css" />
</head>
<body class="register-body">
    <header class="site-header">
        <div class="header-inner">
            <a class="logo" href="${pageContext.request.contextPath}/index.html">Krishak</a>
            <nav class="main-nav" aria-label="Primary">
                <a href="${pageContext.request.contextPath}/index.html">Home</a>
                <a href="${pageContext.request.contextPath}/shop">Product</a>
                <a href="${pageContext.request.contextPath}/index.html#about">About Us</a>
                <a href="${pageContext.request.contextPath}/contact">Contact</a>
            </nav>
        </div>
    </header>

    <main class="auth-page">
        <section class="auth-card auth-card--register" aria-labelledby="register-heading">
            <div class="logo-wrap">
                <div class="logo-circle" aria-hidden="true">
                    <img src="${pageContext.request.contextPath}/image/white_lock.png" alt="" />
                </div>
            </div>

            <div class="welcome">
                <h1 id="register-heading">Create your account</h1>
                <p>Join Krishak and connect with fresh produce from local farmers.</p>
            </div>

            <nav class="switcher" aria-label="Authentication pages">
                <a class="switch-link" href="${pageContext.request.contextPath}/login">Login</a>
                <a class="switch-link active" href="${pageContext.request.contextPath}/register">Register</a>
            </nav>

            <form class="auth-form" action="${pageContext.request.contextPath}/register" method="post">
                <c:if test="${not empty error}">
                    <p class="auth-error"><c:out value="${error}" /></p>
                </c:if>

                <div class="form-group">
                    <label for="register-username">Username</label>
                    <div class="input-wrap">
                        <span class="input-icon" aria-hidden="true">
                            <img class="icon-img" src="${pageContext.request.contextPath}/image/user.png" alt="" />
                        </span>
                        <input id="register-username" type="text" name="username" placeholder="Your username"
                               value="<c:out value='${param.username}' default=''/>" required />
                    </div>
                </div>

                <div class="form-group">
                    <label for="register-email">Email</label>
                    <div class="input-wrap">
                        <span class="input-icon" aria-hidden="true">
                            <img class="icon-img" src="${pageContext.request.contextPath}/image/email.png" alt="" />
                        </span>
                        <input id="register-email" type="email" name="email" placeholder="name@example.com"
                               value="<c:out value='${param.email}' default=''/>" required />
                    </div>
                </div>

                <div class="form-group">
                    <label for="register-password">Password</label>
                    <div class="input-wrap">
                        <span class="input-icon" aria-hidden="true">
                            <img class="icon-img" src="${pageContext.request.contextPath}/image/lock.png" alt="" />
                        </span>
                        <input id="register-password" type="password" name="password"
                               placeholder="At least 8 characters" required />
                    </div>
                </div>

                <div class="form-group">
                    <label for="register-confirm-password">Confirm password</label>
                    <div class="input-wrap">
                        <span class="input-icon" aria-hidden="true">
                            <img class="icon-img" src="${pageContext.request.contextPath}/image/lock.png" alt="" />
                        </span>
                        <input id="register-confirm-password" type="password" name="confirmed_password"
                               placeholder="Re-enter your password" required />
                    </div>
                </div>

                <div class="form-group">
                    <label for="register-phone">Phone</label>
                    <div class="input-wrap">
                        <span class="input-icon" aria-hidden="true">
                            <img class="icon-img" src="${pageContext.request.contextPath}/image/phone.png" alt="" />
                        </span>
                        <input id="register-phone" type="tel" name="phone" placeholder="Your phone number"
                               value="<c:out value='${param.phone}' default=''/>" required />
                    </div>
                </div>

                <button class="submit-btn" type="submit">Create Account</button>
            </form>

            <p class="auth-switch-text">
                Already have an account?
                <a href="${pageContext.request.contextPath}/login">Log in</a>
            </p>

            <p class="auth-footer">
                <a href="${pageContext.request.contextPath}/index.html">Home</a>
                <span class="auth-footer-sep" aria-hidden="true">·</span>
                <a href="${pageContext.request.contextPath}/index.html#about">About Us</a>
            </p>
        </section>
    </main>
</body>
</html>
