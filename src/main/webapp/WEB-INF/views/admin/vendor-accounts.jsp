<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Vendor Accounts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
<div class="admin-app">
    <jsp:include page="sidebar.jsp" />

    <div class="admin-main">
        <div class="vendor-accounts-page">
            <header class="vendor-accounts-page__head">
                <h1 class="vendor-accounts-page__title">Vendor Accounts</h1>
                <form class="vendor-accounts-search" method="get" action="${pageContext.request.contextPath}/admin" role="search">
                    <input type="hidden" name="section" value="accounts" />
                    <span class="vendor-accounts-search__icon" aria-hidden="true">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M11 19a8 8 0 1 0 0-16 8 8 0 0 0 0 16Z" stroke="#6A7282" stroke-width="2" stroke-linecap="round"/>
                            <path d="m21 21-4.3-4.3" stroke="#6A7282" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                    </span>
                    <input class="vendor-accounts-search__input"
                           type="search"
                           name="q"
                           value="<c:out value='${vendorAccountsSearch}' />"
                           placeholder="Search vendors..."
                           autocomplete="off"
                           aria-label="Search vendors" />
                </form>
            </header>

            <c:if test="${not empty vendorAccountFlash}">
                <div class="requests-flash ${vendorAccountFlashOk ? 'requests-flash--ok' : 'requests-flash--err'}" role="status">
                    <p><c:out value="${vendorAccountFlash}" /></p>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty vendorAccountCards}">
                    <p class="vendor-accounts-empty">No approved vendors yet. Approve applications from Vendor Requests first.</p>
                </c:when>
                <c:otherwise>
                    <div class="vendor-accounts-grid">
                        <c:forEach var="v" items="${vendorAccountCards}">
                            <article class="vendor-acct-card">
                                <div class="vendor-acct-card__top">
                                    <div class="vendor-acct-card__identity">
                                        <h2 class="vendor-acct-card__name"><c:out value="${v.name}" /></h2>
                                        <p class="vendor-acct-card__company"><c:out value="${v.company}" /></p>
                                        <p class="vendor-acct-card__contact"><c:out value="${v.email}" /></p>
                                        <p class="vendor-acct-card__contact"><c:out value="${v.phone}" /></p>
                                    </div>
                                    <c:if test="${v.verified}">
                                        <span class="vendor-acct-card__verified" title="Verified">
                                            <img src="${pageContext.request.contextPath}/image/approved.png" alt="Verified" width="28" height="28" />
                                        </span>
                                    </c:if>
                                </div>

                                <div class="vendor-acct-card__stats">
                                    <div class="vendor-acct-card__stat">
                                        <span class="vendor-acct-card__stat-label">Products</span>
                                        <span class="vendor-acct-card__stat-value">${v.products}</span>
                                    </div>
                                    <div class="vendor-acct-card__stat">
                                        <span class="vendor-acct-card__stat-label">Orders</span>
                                        <span class="vendor-acct-card__stat-value">${v.orders}</span>
                                    </div>
                                </div>

                                <div class="vendor-acct-card__footer">
                                    <span class="vendor-acct-card__badge">${v.verified ? 'active' : 'inactive'}</span>
                                    <form class="vendor-acct-card__delete-form" method="post"
                                          action="${pageContext.request.contextPath}/admin"
                                          onsubmit="return confirm('Delete this vendor account permanently? Their products will also be removed.');">
                                        <input type="hidden" name="action" value="deleteVendor" />
                                        <input type="hidden" name="vendorId" value="${v.id}" />
                                        <input type="hidden" name="q" value="<c:out value='${vendorAccountsSearch}' />" />
                                        <button type="submit" class="vendor-acct-card__delete">Delete</button>
                                    </form>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>
