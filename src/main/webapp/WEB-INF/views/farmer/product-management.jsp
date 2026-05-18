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
        <p>Manage your product inventory</p>
    </div>
    <a class="vp-btn-add" href="${ctx}/farmer/product-management#add-product">
        <span class="vp-btn-add__icon" aria-hidden="true">+</span>
        Add Product
    </a>
</header>

<div class="vp-stock-alert" role="alert">
    <span class="vp-stock-alert__icon" aria-hidden="true">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
            <line x1="12" y1="9" x2="12" y2="13" />
            <line x1="12" y1="17" x2="12.01" y2="17" />
        </svg>
    </span>
    <div>
        <strong>Low Stock Alert</strong>
        <span>2 product(s) have low stock (less than 5 units)</span>
    </div>
</div>

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
                    <th scope="col">Actions</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><span class="vp-product-thumb vp-product-thumb--empty" aria-hidden="true"></span></td>
                    <td>
                        <span class="vp-product-name">Organic Tomatoes</span>
                        <span class="vp-product-desc">Fresh organic tomatoes from local farms</span>
                    </td>
                    <td>Vegetables</td>
                    <td>Rs. 4.99</td>
                    <td>150</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Organic Tomatoes">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Organic Tomatoes">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><img class="vp-product-thumb" src="${ctx}/image/organic_carrot.png" alt="" width="48" height="48" /></td>
                    <td>
                        <span class="vp-product-name">Fresh Carrots</span>
                        <span class="vp-product-desc">Sweet and crunchy farm-fresh carrots</span>
                    </td>
                    <td>Vegetables</td>
                    <td>Rs. 3.49</td>
                    <td>200</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Fresh Carrots">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Fresh Carrots">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><img class="vp-product-thumb" src="${ctx}/image/organic_lettuce.png" alt="" width="48" height="48" /></td>
                    <td>
                        <span class="vp-product-name">Green Lettuce</span>
                        <span class="vp-product-desc">Crisp leafy greens for salads</span>
                    </td>
                    <td>Vegetables</td>
                    <td>Rs. 2.99</td>
                    <td class="vp-stock-low">4</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Green Lettuce">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Green Lettuce">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><img class="vp-product-thumb" src="${ctx}/image/red_apple.png" alt="" width="48" height="48" /></td>
                    <td>
                        <span class="vp-product-name">Red Apples</span>
                        <span class="vp-product-desc">Juicy red apples picked at peak ripeness</span>
                    </td>
                    <td>Fruits</td>
                    <td>Rs. 5.99</td>
                    <td>120</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Red Apples">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Red Apples">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><img class="vp-product-thumb" src="${ctx}/image/ripe_banana.png" alt="" width="48" height="48" /></td>
                    <td>
                        <span class="vp-product-name">Bananas</span>
                        <span class="vp-product-desc">Naturally sweet ripe bananas</span>
                    </td>
                    <td>Fruits</td>
                    <td>Rs. 2.49</td>
                    <td>180</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Bananas">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Bananas">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><img class="vp-product-thumb" src="${ctx}/image/fresh_spinach.jpg" alt="" width="48" height="48" /></td>
                    <td>
                        <span class="vp-product-name">Fresh Spinach</span>
                        <span class="vp-product-desc">Nutrient-rich leafy spinach</span>
                    </td>
                    <td>Vegetables</td>
                    <td>Rs. 3.99</td>
                    <td>90</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Fresh Spinach">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Fresh Spinach">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><img class="vp-product-thumb" src="${ctx}/image/fresh_orange.png" alt="" width="48" height="48" /></td>
                    <td>
                        <span class="vp-product-name">Oranges</span>
                        <span class="vp-product-desc">Vitamin C packed citrus oranges</span>
                    </td>
                    <td>Fruits</td>
                    <td>Rs. 4.49</td>
                    <td>140</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Oranges">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Oranges">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><img class="vp-product-thumb" src="${ctx}/image/organic_vegetables.png" alt="" width="48" height="48" /></td>
                    <td>
                        <span class="vp-product-name">Potatoes</span>
                        <span class="vp-product-desc">Versatile farm-fresh potatoes</span>
                    </td>
                    <td>Vegetables</td>
                    <td>Rs. 2.99</td>
                    <td class="vp-stock-low">3</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Potatoes">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Potatoes">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><img class="vp-product-thumb" src="${ctx}/image/fresh_strawberries.png" alt="" width="48" height="48" /></td>
                    <td>
                        <span class="vp-product-name">Strawberries</span>
                        <span class="vp-product-desc">Sweet seasonal strawberries</span>
                    </td>
                    <td>Fruits</td>
                    <td>Rs. 6.99</td>
                    <td>85</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Strawberries">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Strawberries">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
                <tr>
                    <td><img class="vp-product-thumb" src="${ctx}/image/fresh_bell_pepper.png" alt="" width="48" height="48" /></td>
                    <td>
                        <span class="vp-product-name">Bell Peppers</span>
                        <span class="vp-product-desc">Colorful crisp bell peppers</span>
                    </td>
                    <td>Vegetables</td>
                    <td>Rs. 4.99</td>
                    <td>110</td>
                    <td class="vp-product-actions">
                        <button type="button" class="vp-icon-btn" aria-label="Edit Bell Peppers">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                        </button>
                        <button type="button" class="vp-icon-btn" aria-label="Delete Bell Peppers">
                            <img src="${ctx}/image/delete.png" alt="" width="18" height="18" />
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</section>

<jsp:include page="vendor-foot.jsp" />
