<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Request Product Change" />
<c:set var="activeNav" value="product-management" scope="request" />
<jsp:include page="vendor-head.jsp" />

<header class="vp-page-head">
    <h1>Request product change</h1>
    <p>Changes are sent to admin for approval. Current listing stays live until approved.</p>
</header>

<c:if test="${param.error eq 'pending'}">
    <p class="vp-preview-banner" role="alert">This product already has a pending change request.</p>
</c:if>
<c:if test="${pendingModeration}">
    <p class="vp-preview-banner" role="alert">A moderation request is already pending for this product.</p>
</c:if>

<c:if test="${not empty errors}">
    <ul class="farmer-apply-errors">
        <c:forEach var="err" items="${errors}">
            <li><c:out value="${err}" /></li>
        </c:forEach>
    </ul>
</c:if>

<section class="vp-products-card vp-product-edit-card">
    <form method="post" action="${ctx}/farmer/product-edit" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${product.id}" />

        <div class="vp-product-edit-grid">
            <div class="vp-product-edit-col">
                <h2 class="vp-product-edit-heading">Current (live)</h2>
                <dl class="vp-product-edit-dl">
                    <div><dt>Name</dt><dd><c:out value="${product.name}" /></dd></div>
                    <div><dt>Category</dt><dd><c:out value="${product.category}" /></dd></div>
                    <div><dt>Price</dt><dd><c:out value="${product.priceLabel}" /></dd></div>
                    <div><dt>Stock</dt><dd><c:out value="${product.stock}" /></dd></div>
                    <c:if test="${not empty product.description}">
                        <div><dt>Description</dt><dd><c:out value="${product.description}" /></dd></div>
                    </c:if>
                </dl>
            </div>

            <div class="vp-product-edit-col">
                <h2 class="vp-product-edit-heading">Proposed changes</h2>

                <div class="vp-field">
                    <label for="name">Name</label>
                    <input id="name" name="name" type="text" required maxlength="100"
                           value="<c:out value='${empty formName ? product.name : formName}' />" />
                </div>
                <div class="vp-field">
                    <label for="category">Category</label>
                    <c:set var="selectedCategory" value="${not empty formCategory ? formCategory : product.category}" />
                    <select id="category" name="category" class="vp-field__select" required>
                        <option value="" disabled>Select category</option>
                        <c:forEach var="cat" items="${productCategories}">
                            <option value="${cat}" ${cat eq selectedCategory ? 'selected' : ''}><c:out value="${cat}" /></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="vp-field">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="3" maxlength="500"><c:out value="${empty formDescription ? product.description : formDescription}" /></textarea>
                </div>
                <div class="vp-field-row">
                    <div class="vp-field">
                        <label for="price">Price (Rs.)</label>
                        <input id="price" name="price" type="number" step="0.01" min="0.01" required
                               value="<c:out value='${empty formPrice ? product.price : formPrice}' />" />
                    </div>
                    <div class="vp-field">
                        <label for="unit">Unit</label>
                        <input id="unit" name="unit" type="text" required maxlength="20" placeholder="kg"
                               value="<c:out value='${empty formUnit ? product.unit : formUnit}' />" />
                    </div>
                    <div class="vp-field">
                        <label for="stock">Stock</label>
                        <input id="stock" name="stock" type="number" min="0" required
                               value="<c:out value='${empty formStock ? product.stock : formStock}' />" />
                    </div>
                </div>
                <div class="vp-field">
                    <label for="photo">Product image</label>
                    <c:set var="currentPhoto" value="${not empty formPhotoPath ? formPhotoPath : product.photoPath}" />
                    <c:if test="${not empty currentPhoto}">
                        <p class="vp-photo-preview-label">Current image</p>
                        <img class="vp-photo-preview" src="${product.resolvePhotoSrc(ctx)}" alt="" width="120" height="120" />
                    </c:if>
                    <div class="vp-field__file-wrap">
                        <input id="photo" name="photo" type="file" class="vp-field__file" accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp" />
                    </div>
                    <span class="vp-field-hint">Upload a new image to replace the current one (JPG, PNG, WEBP — max 5 MB).</span>
                </div>
            </div>
        </div>

        <div class="vp-product-edit-actions">
            <a class="vp-btn-add vp-btn-add--secondary" href="${ctx}/farmer/product-management">Cancel</a>
            <button type="submit" class="vp-btn-add" ${pendingModeration ? 'disabled' : ''}>Submit for moderation</button>
        </div>
    </form>
</section>

<jsp:include page="vendor-foot.jsp" />
