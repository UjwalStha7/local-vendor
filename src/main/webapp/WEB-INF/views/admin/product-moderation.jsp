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
                <p class="moderation-page__subtitle">Review vendor change requests — compare live data with proposed updates.</p>
                <nav class="moderation-page__filters" aria-label="Filter requests">
                    <c:set var="ctx" value="${pageContext.request.contextPath}" />
                    <c:set var="mf" value="${moderationFilter}" />
                    <a class="mod-filter${mf eq 'pending' ? ' mod-filter--active' : ''}"
                       href="${ctx}/admin?section=moderation&amp;filter=pending">Pending</a>
                    <a class="mod-filter${mf eq 'all' ? ' mod-filter--active' : ''}"
                       href="${ctx}/admin?section=moderation&amp;filter=all">All</a>
                    <a class="mod-filter${mf eq 'approved' ? ' mod-filter--active' : ''}"
                       href="${ctx}/admin?section=moderation&amp;filter=approved">Approved</a>
                    <a class="mod-filter${mf eq 'rejected' ? ' mod-filter--active' : ''}"
                       href="${ctx}/admin?section=moderation&amp;filter=rejected">Rejected</a>
                </nav>
            </header>

            <c:choose>
                <c:when test="${empty moderationProducts}">
                    <p class="moderation-empty">No moderation requests in this view.</p>
                </c:when>
                <c:otherwise>
                    <div class="moderation-grid moderation-grid--compare">
                        <c:forEach var="p" items="${moderationProducts}">
                            <article class="mod-card mod-card--compare">
                                <div class="mod-card__head">
                                    <h2 class="mod-card__title"><c:out value="${p.proposedName}" /></h2>
                                    <dl class="mod-card__meta mod-card__meta--inline">
                                        <div class="mod-card__meta-row">
                                            <dt>Vendor</dt>
                                            <dd><c:out value="${p.vendor}" /></dd>
                                        </div>
                                        <div class="mod-card__meta-row">
                                            <dt>Submitted</dt>
                                            <dd><c:out value="${p.submittedAt}" /></dd>
                                        </div>
                                        <div class="mod-card__meta-row">
                                            <dt>Product ID</dt>
                                            <dd>#<c:out value="${p.productId}" /></dd>
                                        </div>
                                    </dl>
                                </div>

                                <div class="mod-compare">
                                    <div class="mod-compare__labels" aria-hidden="true">
                                        <span class="mod-compare__col-label mod-compare__col-label--before">Before (live)</span>
                                        <span class="mod-compare__col-label mod-compare__col-label--after">Requested change</span>
                                    </div>

                                    <div class="mod-compare__row${p.nameChanged ? ' mod-compare__row--changed' : ''}">
                                        <span class="mod-compare__field">Name</span>
                                        <span class="mod-compare__before"><c:out value="${p.prevName}" /></span>
                                        <span class="mod-compare__after"><c:out value="${p.proposedName}" /></span>
                                    </div>
                                    <div class="mod-compare__row${p.categoryChanged ? ' mod-compare__row--changed' : ''}">
                                        <span class="mod-compare__field">Category</span>
                                        <span class="mod-compare__before"><c:out value="${p.prevCategory}" /></span>
                                        <span class="mod-compare__after"><c:out value="${p.proposedCategory}" /></span>
                                    </div>
                                    <div class="mod-compare__row${p.priceChanged ? ' mod-compare__row--changed' : ''}">
                                        <span class="mod-compare__field">Price</span>
                                        <span class="mod-compare__before"><c:out value="${p.prevPriceLabel}" /></span>
                                        <span class="mod-compare__after"><c:out value="${p.proposedPriceLabel}" /></span>
                                    </div>
                                    <div class="mod-compare__row${p.stockChanged ? ' mod-compare__row--changed' : ''}">
                                        <span class="mod-compare__field">Stock</span>
                                        <span class="mod-compare__before"><c:out value="${p.prevStock}" /></span>
                                        <span class="mod-compare__after"><c:out value="${p.proposedStock}" /></span>
                                    </div>
                                    <c:if test="${p.descriptionChanged or not empty p.prevDescription or not empty p.proposedDescription}">
                                        <div class="mod-compare__row mod-compare__row--block${p.descriptionChanged ? ' mod-compare__row--changed' : ''}">
                                            <span class="mod-compare__field">Description</span>
                                            <span class="mod-compare__before"><c:out value="${empty p.prevDescription ? '—' : p.prevDescription}" /></span>
                                            <span class="mod-compare__after"><c:out value="${empty p.proposedDescription ? '—' : p.proposedDescription}" /></span>
                                        </div>
                                    </c:if>
                                    <c:if test="${p.photoChanged}">
                                        <div class="mod-compare__row mod-compare__row--block mod-compare__row--changed">
                                            <span class="mod-compare__field">Image</span>
                                            <span class="mod-compare__before">
                                                <c:choose>
                                                    <c:when test="${not empty p.prevPhotoPath}">
                                                        <img class="mod-compare__thumb" src="${ctx}${p.prevPhotoPath}" alt="" loading="lazy" />
                                                    </c:when>
                                                    <c:otherwise>—</c:otherwise>
                                                </c:choose>
                                            </span>
                                            <span class="mod-compare__after">
                                                <c:choose>
                                                    <c:when test="${not empty p.proposedPhotoPath}">
                                                        <img class="mod-compare__thumb" src="${ctx}${p.proposedPhotoPath}" alt="" loading="lazy" />
                                                    </c:when>
                                                    <c:otherwise>—</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                    </c:if>
                                </div>

                                <div class="mod-card__footer">
                                    <c:choose>
                                        <c:when test="${p.status eq 'pending'}">
                                            <span class="mod-badge mod-badge--pending">
                                                <img src="${ctx}/image/pending.png" alt="" width="16" height="16" />
                                                Pending review
                                            </span>
                                            <div class="mod-card__actions">
                                                <form method="post" action="${ctx}/admin" class="mod-card__form">
                                                    <input type="hidden" name="action" value="moderateApprove" />
                                                    <input type="hidden" name="id" value="${p.id}" />
                                                    <input type="hidden" name="filter" value="${moderationFilter}" />
                                                    <button type="submit" class="mod-btn mod-btn--approve">Approve changes</button>
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
