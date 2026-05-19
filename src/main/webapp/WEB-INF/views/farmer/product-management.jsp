<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Product Management" />
<c:set var="activeNav" value="product-management" scope="request" />
<jsp:include page="vendor-head.jsp" />

<c:if test="${previewMode}">
    <p class="vp-preview-banner" role="status">
        Preview mode — not logged in. Sign in as a vendor to manage products at
        <a href="${ctx}/farmer/product-management">/farmer/product-management</a>.
    </p>
</c:if>

<header class="vp-page-head vp-page-head--toolbar">
    <div>
        <h1>Product Management</h1>
        <p>Manage your product inventory. Edits are sent to admin for approval.</p>
    </div>
</header>

<c:if test="${not empty moderationNotice}">
    <p class="vp-preview-banner" role="status"><c:out value="${moderationNotice}" /></p>
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
                            <td colspan="7" class="vp-table-empty">No products yet. Add products in the database or contact admin.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${vendorProducts}">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty p.photoPath}">
                                            <img class="vp-product-thumb" src="${ctx}${p.photoPath}" alt="" width="48" height="48" />
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
                                    <c:if test="${not previewMode}">
                                        <a class="vp-icon-btn" href="${ctx}/farmer/product-edit?id=${p.id}"
                                           aria-label="Request change for ${p.name}">
                                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                                        </a>
                                    </c:if>
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
