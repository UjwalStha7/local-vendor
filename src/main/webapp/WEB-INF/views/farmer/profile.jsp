<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Vendor Profile" />
<c:set var="activeNav" value="profile" scope="request" />
<jsp:include page="vendor-head.jsp" />

<c:if test="${previewMode}">
    <p class="vp-preview-banner" role="status">
        Preview mode — not logged in. Sign in as a vendor to edit your profile at
        <a href="${ctx}/farmer/profile">/farmer/profile</a>.
    </p>
</c:if>
<c:if test="${param.saved == '1'}">
    <p class="vp-preview-banner vp-preview-banner--ok" role="status">Profile saved successfully.</p>
</c:if>
<c:if test="${not empty profileError}">
    <p class="vp-preview-banner" role="alert"><c:out value="${profileError}" /></p>
</c:if>
<c:set var="logoSrc" value="${profile.resolveLogoSrc(ctx)}" />

<header class="vp-page-head vp-page-head--profile">
    <div>
        <h1>Vendor Profile</h1>
        <p>Manage your shop information and public profile</p>
    </div>
    <c:choose>
        <c:when test="${editMode}">
            <c:if test="${not previewMode}">
                <a class="vp-btn-ghost" href="${ctx}/farmer/profile">Cancel</a>
            </c:if>
            <c:if test="${previewMode}">
                <a class="vp-btn-ghost" href="${ctx}/farmerprofilepreview">Cancel</a>
            </c:if>
        </c:when>
        <c:otherwise>
            <c:if test="${previewMode}">
                <a class="vp-btn-edit" href="${ctx}/farmerprofilepreview?edit=1">Edit Profile</a>
            </c:if>
            <c:if test="${not previewMode}">
                <a class="vp-btn-edit" href="${ctx}/farmer/profile?edit=1">Edit Profile</a>
            </c:if>
        </c:otherwise>
    </c:choose>
</header>

<form id="vendorProfileForm" class="vp-profile-layout"
      method="post"
      enctype="multipart/form-data"
      action="${previewMode ? '#' : ctx.concat('/farmer/profile')}"
      data-preview="${previewMode}"
      data-ctx="${ctx}">

    <div class="vp-profile-main">
        <section class="vp-profile-panel" aria-labelledby="shop-info-heading">
            <h2 id="shop-info-heading" class="vp-profile-panel__title">Shop Information</h2>
            <div class="vp-field">
                <label class="vp-field__label" for="shopName">Shop Name <span class="vp-req">*</span></label>
                <c:choose>
                    <c:when test="${editMode}">
                        <input type="text" id="shopName" name="shopName" class="vp-field__input"
                               value="${profile.shopName}" required data-preview-field />
                    </c:when>
                    <c:otherwise>
                        <div class="vp-field__display" data-preview-source="shopName">${profile.shopName}</div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="vp-field">
                <label class="vp-field__label" for="shopBio">Shop Bio</label>
                <c:choose>
                    <c:when test="${editMode}">
                        <textarea id="shopBio" name="shopBio" class="vp-field__input vp-field__textarea" rows="3"
                                  data-preview-field>${profile.shopBio}</textarea>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${profile.hasShopBio}">
                                <div class="vp-field__display" data-preview-source="shopBio"><c:out value="${profile.shopBio}" /></div>
                            </c:when>
                            <c:otherwise>
                                <p class="vp-field__hint">No shop bio added yet.</p>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <section class="vp-profile-panel" aria-labelledby="contact-info-heading">
            <h2 id="contact-info-heading" class="vp-profile-panel__title">Contact Information</h2>
            <div class="vp-field">
                <label class="vp-field__label" for="email">Email <span class="vp-req">*</span></label>
                <c:choose>
                    <c:when test="${editMode}">
                        <input type="email" id="email" class="vp-field__input vp-field__input--readonly"
                               value="${profile.email}" readonly aria-readonly="true" />
                        <span class="vp-field__hint">Email cannot be changed here. Contact support if you need to update it.</span>
                    </c:when>
                    <c:otherwise>
                        <div class="vp-field__display" data-preview-source="email">${profile.email}</div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="vp-field">
                <label class="vp-field__label" for="phone">Phone Number</label>
                <c:choose>
                    <c:when test="${editMode}">
                        <input type="tel" id="phone" name="phone" class="vp-field__input"
                               value="${profile.phone}" data-preview-field />
                    </c:when>
                    <c:otherwise>
                        <div class="vp-field__display" data-preview-source="phone">${profile.phone}</div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="vp-field">
                <label class="vp-field__label" for="address">Address</label>
                <c:choose>
                    <c:when test="${editMode}">
                        <input type="text" id="address" name="address" class="vp-field__input"
                               value="${profile.address}" data-preview-field />
                    </c:when>
                    <c:otherwise>
                        <div class="vp-field__display vp-field__display--empty" data-preview-source="address">
                            <c:out value="${empty profile.address ? '' : profile.address}" />
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <section class="vp-profile-panel" aria-labelledby="logo-heading">
            <h2 id="logo-heading" class="vp-profile-panel__title">Shop Logo</h2>
            <c:if test="${editMode}">
                <div class="vp-field">
                    <label class="vp-field__label" for="logo">Upload logo</label>
                    <div class="vp-field__file-wrap">
                        <input type="file" id="logo" name="logo" class="vp-field__file"
                               accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp"
                               data-logo-file-input />
                    </div>
                    <span class="vp-field__hint">JPG, PNG, or WEBP — max 5 MB. Leave empty to keep your current logo.</span>
                </div>
            </c:if>
            <div class="vp-logo-preview-row">
                <span class="vp-logo-preview-row__label">Logo Preview</span>
                <img id="logoPreviewImg" class="vp-logo-preview-row__img"
                     src="${logoSrc}"
                     alt="Shop logo preview"
                     width="56" height="56"
                     data-fallback="${ctx}/image/fresh_apple.png" />
            </div>
        </section>

        <c:if test="${editMode and not previewMode}">
            <div class="vp-profile-form-actions">
                <button type="submit" class="vp-btn-edit">Save Profile</button>
            </div>
        </c:if>
    </div>

    <aside class="vp-profile-aside" aria-label="Profile preview and stats">
        <section class="vp-profile-panel vp-customer-preview" aria-labelledby="customer-preview-heading">
            <h2 id="customer-preview-heading" class="vp-profile-panel__title">Customer View Preview</h2>
            <p class="vp-customer-preview__lead">This is how your shop profile will appear to customers</p>

            <div class="vp-customer-card">
                <div class="vp-customer-card__head">
                    <img id="previewLogo" class="vp-customer-card__logo"
                         src="${logoSrc}"
                         alt=""
                         width="56" height="56"
                         data-fallback="${ctx}/image/fresh_apple.png" />
                    <div>
                        <p id="previewShopName" class="vp-customer-card__name">${profile.shopName}</p>
                        <c:if test="${profile.verified}">
                            <p class="vp-customer-card__verified">
                                <img src="${ctx}/image/approved.png" alt="" width="14" height="14" />
                                Verified Vendor
                            </p>
                        </c:if>
                    </div>
                </div>
                <c:if test="${profile.hasShopBio or editMode}">
                    <p id="previewBio" class="vp-customer-card__bio" data-preview-bio ${empty profile.shopBio ? 'hidden' : ''}><c:out value="${profile.shopBio}" /></p>
                </c:if>
                <ul class="vp-customer-card__contacts">
                    <li>
                        <img src="${ctx}/image/green_email.png" alt="" width="16" height="16" />
                        <span id="previewEmail">${profile.email}</span>
                    </li>
                    <li>
                        <img src="${ctx}/image/phone.png" alt="" width="16" height="16" />
                        <span id="previewPhone">${profile.phone}</span>
                    </li>
                    <li>
                        <img src="${ctx}/image/location.png" alt="" width="16" height="16" />
                        <span id="previewAddress"><c:out value="${profile.address}" /></span>
                    </li>
                </ul>
            </div>
        </section>

        <section class="vp-profile-panel" aria-labelledby="quick-stats-heading">
            <h2 id="quick-stats-heading" class="vp-profile-panel__title">Quick Stats</h2>
            <ul class="vp-quick-stats">
                <li>
                    <span class="vp-quick-stats__icon" aria-hidden="true">
                        <img src="${ctx}/image/vendor.png" alt="" width="18" height="18" />
                    </span>
                    <span>
                        <span class="vp-quick-stats__label">Member Since</span>
                        <span class="vp-quick-stats__value">${profile.memberSince}</span>
                    </span>
                </li>
                <li>
                    <span class="vp-quick-stats__icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                        </svg>
                    </span>
                    <span>
                        <span class="vp-quick-stats__label">Response Time</span>
                        <span class="vp-quick-stats__value">${profile.responseTime}</span>
                    </span>
                </li>
            </ul>
        </section>
    </aside>
</form>

<script src="${ctx}/js/farmer-profile.js"></script>
<jsp:include page="vendor-foot.jsp" />
