<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:if test="${empty storeName}"><c:set var="storeName" value="${vendorName}" /></c:if>
<c:if test="${empty storeName}"><c:set var="storeName" value="FreshHarvest Farms" /></c:if>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><c:out value="${pageTitle}" /> — Vendor Portal</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${ctx}/css/farmer-dashboard.css" />
</head>
<body class="vp-body">
<div class="vp-shell">
    <jsp:include page="vendor-sidebar.jsp" />
    <div class="vp-main">
        <jsp:include page="vendor-topbar.jsp" />
        <main class="vp-content">
