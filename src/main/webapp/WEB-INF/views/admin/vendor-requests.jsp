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
                    <a class="requests-filter${f eq 'approved' ? ' requests-filter--active' : ''}"
                       href="${ctx}/admin?section=requests&amp;filter=approved">Approved</a>
                </nav>
            </header>

            <c:if test="${not empty approvalFlash}">
                <div class="requests-flash ${approvalFlash.success ? 'requests-flash--ok' : 'requests-flash--err'}" role="status">
                    <p><c:out value="${approvalFlash.message}" /></p>
                    <c:if test="${approvalFlash.success}">
                        <p><strong>Login email:</strong> <c:out value="${approvalFlash.vendorLoginEmail}" /></p>
                        <p><strong>Temporary password:</strong> <c:out value="${approvalFlash.temporaryPassword}" /></p>
                        <p class="requests-flash__hint">Share these credentials with the farmer. They sign in at Login using the @krishak.np email.</p>
                    </c:if>
                </div>
            </c:if>

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
                                    <c:if test="${not empty row.applicantName}">
                                        <span class="requests-contact__name"><c:out value="${row.applicantName}" /></span>
                                    </c:if>
                                    <c:if test="${not empty row.farmName}">
                                        <span class="requests-contact__farm"><c:out value="${row.farmName}" /></span>
                                    </c:if>
                                    <span class="requests-contact__email"><c:out value="${row.email}" /></span>
                                    <span class="requests-contact__phone"><c:out value="${row.phone}" /></span>
                                    <c:if test="${not empty row.vendorLoginEmail}">
                                        <span class="requests-contact__vendor-login">Vendor login: <c:out value="${row.vendorLoginEmail}" /></span>
                                    </c:if>
                                </div>
                            </td>
                            <td class="requests-table__date">${row.submittedAt}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${row.status eq 'pending'}">
                                        <span class="requests-badge requests-badge--pending">Pending</span>
                                    </c:when>
                                    <c:when test="${row.status eq 'approved'}">
                                        <span class="requests-badge requests-badge--approved">Approved</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="requests-badge requests-badge--contacted">Contacted</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${row.status eq 'approved'}">
                                        <span class="requests-muted-action">Vendor account active</span>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="requests-actions">
                                            <c:if test="${row.status eq 'pending'}">
                                                <form class="requests-action-form" method="post" action="${ctx}/admin">
                                                    <input type="hidden" name="action" value="contact" />
                                                    <input type="hidden" name="id" value="${row.id}" />
                                                    <input type="hidden" name="filter" value="${requestFilter}" />
                                                    <button type="submit" class="requests-btn-contact">Make Contact</button>
                                                </form>
                                            </c:if>
                                            <form class="requests-action-form" method="post" action="${ctx}/admin"
                                                  onsubmit="return confirm('Approve this farmer and create their @krishak.np login?');">
                                                <input type="hidden" name="action" value="approve" />
                                                <input type="hidden" name="id" value="${row.id}" />
                                                <input type="hidden" name="filter" value="${requestFilter}" />
                                                <button type="submit" class="requests-btn-approve">Approve as vendor</button>
                                            </form>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty vendorRequestRows}">
                        <tr>
                            <td colspan="4" class="requests-empty">No applications in this view.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
