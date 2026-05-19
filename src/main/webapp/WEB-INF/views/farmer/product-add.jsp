<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Add Product" />
<c:set var="activeNav" value="product-management" scope="request" />
<jsp:include page="vendor-head.jsp" />

<header class="vp-page-head">
    <h1>Add product</h1>
    <p>Your listing will be sent to admin for approval before it appears in the store.</p>
</header>

<c:if test="${not empty formError}">
    <p class="vp-preview-banner" role="alert"><c:out value="${formError}" /></p>
</c:if>
<c:if test="${pendingNewProduct}">
    <p class="vp-preview-banner" role="alert">You already have a new product waiting for admin approval.</p>
</c:if>

<c:if test="${not empty errors}">
    <ul class="farmer-apply-errors">
        <c:forEach var="err" items="${errors}">
            <li><c:out value="${err}" /></li>
        </c:forEach>
    </ul>
</c:if>

<section class="vp-products-card vp-product-edit-card">
    <form method="post" action="${ctx}/farmer/product-add">
        <div class="vp-product-edit-col vp-product-edit-col--full">
            <div class="vp-field">
                <label for="name">Name</label>
                <input id="name" name="name" type="text" required maxlength="100"
                       value="<c:out value='${formName}' />" />
            </div>
            <div class="vp-field">
                <label for="category">Category</label>
                <input id="category" name="category" type="text" required maxlength="50"
                       value="<c:out value='${formCategory}' />" />
            </div>
            <div class="vp-field">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="3" maxlength="500"><c:out value="${formDescription}" /></textarea>
            </div>
            <div class="vp-field-row">
                <div class="vp-field">
                    <label for="price">Price (Rs.)</label>
                    <input id="price" name="price" type="number" step="0.01" min="0.01" required
                           value="<c:out value='${formPrice}' />" />
                </div>
                <div class="vp-field">
                    <label for="unit">Unit</label>
                    <input id="unit" name="unit" type="text" required maxlength="20" placeholder="kg"
                           value="<c:out value='${formUnit}' />" />
                </div>
                <div class="vp-field">
                    <label for="stock">Stock</label>
                    <input id="stock" name="stock" type="number" min="0" required
                           value="<c:out value='${formStock}' />" />
                </div>
            </div>
            <div class="vp-field">
                <label for="photoPath">Image path (optional)</label>
                <input id="photoPath" name="photoPath" type="text" maxlength="255" placeholder="/image/your_product.png"
                       value="<c:out value='${formPhotoPath}' />" />
                <span class="vp-field-hint">Example: /image/organic_tomatoes.jpeg</span>
            </div>
        </div>

        <div class="vp-product-edit-actions">
            <a class="vp-btn-add vp-btn-add--secondary" href="${ctx}/farmer/product-management">Cancel</a>
            <button type="submit" class="vp-btn-add" ${pendingNewProduct ? 'disabled' : ''}>Submit for moderation</button>
        </div>
    </form>
</section>

<jsp:include page="vendor-foot.jsp" />
