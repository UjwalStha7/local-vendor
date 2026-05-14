<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Product Moderation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
<div class="admin-app">
    <jsp:include page="sidebar.jsp" />

    <div class="admin-main">
        <div class="moderation-page">
            <header class="moderation-page__head">
                <h1 class="moderation-page__title">Product Moderation</h1>
                <nav class="moderation-page__filters" aria-label="Filter products">
                    <c:set var="ctx" value="${pageContext.request.contextPath}" />
                    <c:set var="mf" value="${moderationFilter}" />
                    <a class="mod-filter${mf eq 'all' ? ' mod-filter--active' : ''}"
                       href="${ctx}/admin?section=moderation&amp;filter=all">All</a>
                    <a class="mod-filter${mf eq 'pending' ? ' mod-filter--active' : ''}"
                       href="${ctx}/admin?section=moderation&amp;filter=pending">Pending</a>
                    <a class="mod-filter${mf eq 'approved' ? ' mod-filter--active' : ''}"
                       href="${ctx}/admin?section=moderation&amp;filter=approved">Approved</a>
                    <a class="mod-filter${mf eq 'rejected' ? ' mod-filter--active' : ''}"
                       href="${ctx}/admin?section=moderation&amp;filter=rejected">Rejected</a>
                </nav>
            </header>

            <c:choose>
                <c:when test="${empty moderationProducts}">
                    <p class="moderation-empty">No products in this view.</p>
                </c:when>
                <c:otherwise>
                    <div class="moderation-grid">
                        <c:forEach var="p" items="${moderationProducts}">
                            <article class="mod-card">
                                <div class="mod-card__media">
                                    <img src="${ctx}${p.imagePath}" alt="" loading="lazy" />
                                </div>
                                <div class="mod-card__body">
                                    <h2 class="mod-card__title"><c:out value="${p.name}" /></h2>
                                    <dl class="mod-card__meta">
                                        <div class="mod-card__meta-row">
                                            <dt>Vendor</dt>
                                            <dd><c:out value="${p.vendor}" /></dd>
                                        </div>
                                        <div class="mod-card__meta-row">
                                            <dt>Category</dt>
                                            <dd><c:out value="${p.category}" /></dd>
                                        </div>
                                        <div class="mod-card__meta-row">
                                            <dt>Price</dt>
                                            <dd><c:out value="${p.priceLabel}" /></dd>
                                        </div>
                                        <div class="mod-card__meta-row">
                                            <dt>Submitted</dt>
                                            <dd><c:out value="${p.submittedAt}" /></dd>
                                        </div>
                                    </dl>

                                    <div class="mod-card__footer">
                                        <c:choose>
                                            <c:when test="${p.status eq 'pending'}">
                                                <span class="mod-badge mod-badge--pending">
                                                    <img src="${ctx}/image/pending.png" alt="" width="16" height="16" />
                                                    Pending Review
                                                </span>
                                                <div class="mod-card__actions">
                                                    <form method="post" action="${ctx}/admin" class="mod-card__form">
                                                        <input type="hidden" name="action" value="moderateApprove" />
                                                        <input type="hidden" name="id" value="${p.id}" />
                                                        <input type="hidden" name="filter" value="${moderationFilter}" />
                                                        <button type="submit" class="mod-btn mod-btn--approve">Approve</button>
                                                    </form>
                                                    <form method="post" action="${ctx}/admin" class="mod-card__form">
                                                        <input type="hidden" name="action" value="moderateReject" />
                                                        <input type="hidden" name="id" value="${p.id}" />
                                                        <input type="hidden" name="filter" value="${moderationFilter}" />
                                                        <button type="submit" class="mod-btn mod-btn--reject">Reject</button>
                                                    </form>
                                                </div>
                                            </c:when>
                                            <c:when test="${p.status eq 'approved'}">
                                                <span class="mod-badge mod-badge--approved">
                                                    <img src="${ctx}/image/approved.png" alt="" width="16" height="16" />
                                                    Approved
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="mod-badge mod-badge--rejected">Rejected</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
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
