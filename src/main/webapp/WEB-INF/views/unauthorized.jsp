<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Unauthorized</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body style="font-family: system-ui, sans-serif; padding: 48px; text-align: center;">
    <h1>Access denied</h1>
    <p>You do not have permission to open this page.</p>
    <p><a href="${pageContext.request.contextPath}/login">Back to login</a></p>
</body>
</html>
