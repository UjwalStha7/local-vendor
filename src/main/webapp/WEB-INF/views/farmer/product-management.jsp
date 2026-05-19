<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Product Management" />
<c:set var="activeNav" value="product-management" scope="request" />
<jsp:include page="vendor-head.jsp" />

<header class="vp-page-head vp-page-head--toolbar">
    <div>
        <h1>Product Management</h1>
        <p>Delete products immediately. Add or edit sends a request to admin moderation.</p>
    </div>
    <div class="vp-page-head__tools">
        <form class="vp-product-search" method="get" action="${ctx}/farmer/product-management" role="search">
            <img src="${ctx}/image/search.png" alt="" width="18" height="18" aria-hidden="true" />
            <input type="search" name="q" placeholder="Search by product name..."
                   value="<c:out value='${productSearch}' />"
                   aria-label="Search products" />
        </form>
        <a class="vp-btn-add" href="${ctx}/farmer/product-add">
            <span class="vp-btn-add__icon" aria-hidden="true">+</span>
            Add product
        </a>
    </div>
</header>

<c:if test="${not empty moderationNotice}">
    <p class="vp-flash-notice" role="status"><c:out value="${moderationNotice}" /></p>
</c:if>
<c:if test="${pendingNewProduct}">
    <p class="vp-preview-banner" role="status">You have a new product waiting for admin approval.</p>
</c:if>

<section class="vp-products-card" aria-label="Product inventory">
    <div class="vp-table-wrap">
        <table class="vp-table vp-products-table">
            <thead>
                <tr>
                    <th scope="col">Image</th>
                    <th scope="col">Name</th>
                    <th scope="col">Category</th>
                    <th scope="col">Price</th>
                    <th scope="col">Stock</th>
                    <th scope="col">Status</th>
                    <th scope="col">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty vendorProducts}">
                        <tr>
                            <td colspan="7" class="vp-table-empty">No products found. Add a product to submit it for admin approval.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${vendorProducts}">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty p.photoPath}">
                                            <img class="vp-product-thumb" src="${p.resolvePhotoSrc(ctx)}" alt="" width="48" height="48" />
                                        </c:when>
                                        <c:otherwise>
                                            <span class="vp-product-thumb vp-product-thumb--empty" aria-hidden="true"></span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="vp-product-name"><c:out value="${p.name}" /></span>
                                    <c:if test="${not empty p.description}">
                                        <span class="vp-product-desc"><c:out value="${p.description}" /></span>
                                    </c:if>
                                </td>
                                <td><c:out value="${p.category}" /></td>
                                <td><c:out value="${p.priceLabel}" /></td>
                                <td><c:out value="${p.stock}" /></td>
                                <td>
                                    <c:if test="${p.pendingModeration}">
                                        <span class="vp-badge vp-badge--pending">Pending review</span>
                                    </c:if>
                                </td>
                                <td class="vp-product-actions">
                                    <a class="vp-icon-btn" href="${ctx}/farmer/product-edit?id=${p.id}"
                                           title="Request changes"
                                           aria-label="Request change for ${p.name}">
                                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                                        </a>
                                        <form class="vp-inline-form" method="post"
                                              action="${ctx}/farmer/product-management"
                                              onsubmit="return confirm('Delete this product permanently? This cannot be undone.');">
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="id" value="${p.id}" />
                                            <input type="hidden" name="q" value="<c:out value='${productSearch}' />" />
                                            <button type="submit" class="vp-icon-btn vp-icon-btn--danger" title="Delete product"
                                                    aria-label="Delete ${p.name}">
                                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M3 6h18"/><path d="M8 6V4h8v2"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/><path d="M10 11v6"/><path d="M14 11v6"/></svg>
                                            </button>
                                        </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</section>

<jsp:include page="vendor-foot.jsp" />
