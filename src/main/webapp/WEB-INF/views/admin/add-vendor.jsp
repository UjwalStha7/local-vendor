<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Add New Vendor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
<div class="admin-app">
    <jsp:include page="sidebar.jsp" />

    <div class="admin-main">
        <div class="vendor-form-page">
            <header class="vendor-form-page__intro">
                <h1 class="vendor-form-page__title">Add New Vendor</h1>
                <p class="vendor-form-page__subtitle">Fill in the vendor information below</p>
            </header>

            <c:if test="${param.ok eq '1'}">
                <p class="vendor-form-page__notice" role="status">Vendor information was submitted.</p>
            </c:if>

            <div class="vendor-form-card">
                <form class="vendor-form"
                      method="post"
                      action="${pageContext.request.contextPath}/admin"
                      enctype="multipart/form-data">
                    <input type="hidden" name="action" value="saveVendor" />

                    <div class="vendor-form__grid">
                        <div class="vendor-field">
                            <label class="vendor-field__label" for="vendorName">Vendor Name <span class="vendor-field__req" aria-hidden="true">*</span></label>
                            <input class="vendor-field__input" id="vendorName" name="vendorName" type="text" required
                                   placeholder="Enter vendor name" autocomplete="name" />
                        </div>
                        <div class="vendor-field">
                            <label class="vendor-field__label" for="companyName">Company Name <span class="vendor-field__req" aria-hidden="true">*</span></label>
                            <input class="vendor-field__input" id="companyName" name="companyName" type="text" required
                                   placeholder="Enter company name" autocomplete="organization" />
                        </div>

                        <div class="vendor-field">
                            <label class="vendor-field__label" for="email">Email Address <span class="vendor-field__req" aria-hidden="true">*</span></label>
                            <input class="vendor-field__input" id="email" name="email" type="email" required
                                   placeholder="vendor@example.com" autocomplete="email" />
                        </div>
                        <div class="vendor-field">
                            <label class="vendor-field__label" for="phone">Phone Number <span class="vendor-field__req" aria-hidden="true">*</span></label>
                            <input class="vendor-field__input" id="phone" name="phone" type="tel" required
                                   placeholder="+1 234 567 8900" autocomplete="tel" />
                        </div>

                        <div class="vendor-field">
                            <label class="vendor-field__label" for="registrationNo">Business Registration Number <span class="vendor-field__req" aria-hidden="true">*</span></label>
                            <input class="vendor-field__input" id="registrationNo" name="registrationNo" type="text" required
                                   placeholder="Enter registration number" />
                        </div>
                        <div class="vendor-field">
                            <label class="vendor-field__label" for="city">City <span class="vendor-field__req" aria-hidden="true">*</span></label>
                            <input class="vendor-field__input" id="city" name="city" type="text" required
                                   placeholder="Enter city" autocomplete="address-level2" />
                        </div>

                        <div class="vendor-field vendor-field--full">
                            <label class="vendor-field__label" for="address">Address <span class="vendor-field__req" aria-hidden="true">*</span></label>
                            <textarea class="vendor-field__input vendor-field__textarea" id="address" name="address" rows="4" required
                                      placeholder="Enter full address"></textarea>
                        </div>

                        <div class="vendor-field">
                            <label class="vendor-field__label" for="category">Category <span class="vendor-field__req" aria-hidden="true">*</span></label>
                            <select class="vendor-field__input vendor-field__select" id="category" name="category" required>
                                <option value="" disabled selected>Select category</option>
                                <option value="vegetables">Vegetables</option>
                                <option value="fruits">Fruits</option>
                                <option value="grains">Grains</option>
                                <option value="dairy">Dairy</option>
                                <option value="meat">Meat &amp; Poultry</option>
                                <option value="other">Other</option>
                            </select>
                        </div>
                        <div class="vendor-field">
                            <span class="vendor-field__label" id="documents-label">Upload Documents</span>
                            <div class="vendor-field__file-wrap">
                                <input class="vendor-field__file" id="documents" name="documents" type="file"
                                       aria-labelledby="documents-label" accept=".pdf,.png,.jpg,.jpeg,.doc,.docx" />
                            </div>
                        </div>

                        <div class="vendor-field vendor-field--full">
                            <label class="vendor-field__label vendor-field__label--optional" for="description">Description (Optional)</label>
                            <textarea class="vendor-field__input vendor-field__textarea" id="description" name="description" rows="4"
                                      placeholder="Add any additional information"></textarea>
                        </div>
                    </div>

                    <div class="vendor-form__actions">
                        <a class="vendor-form__btn vendor-form__btn--cancel" href="${pageContext.request.contextPath}/admin?section=dashboard">Cancel</a>
                        <button class="vendor-form__btn vendor-form__btn--submit" type="submit">Submit Vendor</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
