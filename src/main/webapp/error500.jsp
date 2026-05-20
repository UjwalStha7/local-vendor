<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>500 - Internal Server Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/error.css" />
  </head>
  <body>
    <div class="container">
      <h1>500</h1>
      <h2>Something Went Wrong</h2>
      <p>An unexpected error occurred on the server. Please try again later.</p>
      <a href="${pageContext.request.contextPath}/" class="btn">Go to Home</a>
    </div>
  </body>
</html>
