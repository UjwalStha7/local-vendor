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
                <p class="requests-page__subtitle">Review farmer applications and approve to create vendor accounts.</p>
            </header>

            <c:if test="${not empty approvalFlash}">
                <div class="requests-flash ${approvalFlash.success ? 'requests-flash--ok' : 'requests-flash--err'}" role="status">
                    <p><c:out value="${approvalFlash.message}" /></p>
                    <c:if test="${approvalFlash.success and not empty approvalFlash.vendorLoginEmail}">
                        <p><strong>Login email:</strong> <c:out value="${approvalFlash.vendorLoginEmail}" /></p>
                        <p><strong>Temporary password:</strong> <c:out value="${approvalFlash.temporaryPassword}" /></p>
                        <p class="requests-flash__hint">Share these credentials with the farmer. They can sign in at Login using the @krishak.np email. The vendor will appear under Vendor Accounts.</p>
                    </c:if>
                </div>
            </c:if>

            <div class="requests-table-wrap">
                <table class="requests-table">
                    <thead>
                    <tr>
                        <th scope="col">Applicant</th>
                        <th scope="col">Date</th>
                        <th scope="col">Status</th>
                        <th scope="col">Action</th>
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
                                </div>
                            </td>
                            <td class="requests-table__date"><c:out value="${row.submittedAt}" /></td>
                            <td>
                                <span class="requests-badge requests-badge--pending">Pending</span>
                            </td>
                            <td>
                                <c:set var="ctx" value="${pageContext.request.contextPath}" />
                                <div class="requests-actions">
                                    <form class="requests-action-form" method="post" action="${ctx}/admin"
                                          onsubmit="return confirm('Approve this application and create a vendor account?');">
                                        <input type="hidden" name="action" value="approve" />
                                        <input type="hidden" name="id" value="${row.id}" />
                                        <button type="submit" class="requests-btn-approve">Approve</button>
                                    </form>
                                    <form class="requests-action-form" method="post" action="${ctx}/admin"
                                          onsubmit="return confirm('Reject this application? The applicant can apply again with the same email later.');">
                                        <input type="hidden" name="action" value="reject" />
                                        <input type="hidden" name="id" value="${row.id}" />
                                        <button type="submit" class="requests-btn-reject">Reject</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty vendorRequestRows}">
                        <tr>
                            <td colspan="4" class="requests-empty">No pending vendor applications.</td>
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
