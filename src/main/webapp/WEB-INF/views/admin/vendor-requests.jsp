<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Vendor Requests</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
<div class="admin-app">
    <jsp:include page="sidebar.jsp" />

    <div class="admin-main">
        <div class="requests-page">
            <header class="requests-page__head">
                <h1 class="requests-page__title">Vendor Requests</h1>
                <nav class="requests-page__filters" aria-label="Filter by status">
                    <c:set var="ctx" value="${pageContext.request.contextPath}" />
                    <c:set var="f" value="${requestFilter}" />
                    <a class="requests-filter${f eq 'all' ? ' requests-filter--active' : ''}"
                       href="${ctx}/admin?section=requests&amp;filter=all">All</a>
                    <a class="requests-filter${f eq 'pending' ? ' requests-filter--active' : ''}"
                       href="${ctx}/admin?section=requests&amp;filter=pending">Pending</a>
                    <a class="requests-filter${f eq 'contacted' ? ' requests-filter--active' : ''}"
                       href="${ctx}/admin?section=requests&amp;filter=contacted">Contacted</a>
                </nav>
            </header>

            <div class="requests-table-wrap">
                <table class="requests-table">
                    <thead>
                    <tr>
                        <th scope="col">Contact Info</th>
                        <th scope="col">Date</th>
                        <th scope="col">Status</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="row" items="${vendorRequestRows}">
                        <tr>
                            <td>
                                <div class="requests-contact">
                                    <span class="requests-contact__email">${row.email}</span>
                                    <span class="requests-contact__phone">${row.phone}</span>
                                </div>
                            </td>
                            <td class="requests-table__date">${row.submittedAt}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${row.status eq 'pending'}">
                                        <span class="requests-badge requests-badge--pending">Pending</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="requests-badge requests-badge--contacted">Contacted</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${row.status eq 'pending'}">
                                        <form class="requests-action-form" method="post" action="${ctx}/admin">
                                            <input type="hidden" name="action" value="contact" />
                                            <input type="hidden" name="id" value="${row.id}" />
                                            <input type="hidden" name="filter" value="${requestFilter}" />
                                            <button type="submit" class="requests-btn-contact">Make Contact</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="requests-muted-action">Already contacted</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
