<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
<div class="admin-app">
    <jsp:include page="sidebar.jsp" />

    <div class="admin-main">
        <header class="admin-topbar">
            <div class="admin-topbar__titles">
                <h1 class="admin-topbar__title">${placeholderTitle}</h1>
                <p class="admin-topbar__subtitle">This section will be available soon.</p>
            </div>
        </header>
        <main class="admin-content">
            <p class="admin-placeholder-note">Use the sidebar to return to the dashboard or vendor requests.</p>
        </main>
    </div>
</div>
</body>
</html>
