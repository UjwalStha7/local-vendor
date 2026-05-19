<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Krishak — Sell with us</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body class="farmer-apply-body">
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<header class="farmer-apply-header">
    <div class="farmer-apply-header__inner">
        <a class="farmer-apply-brand" href="${ctx}/customer/home">Krishak</a>
        <nav class="farmer-apply-nav" aria-label="Farmer apply">
            <a href="${ctx}/customer/home">Home</a>
            <a href="${ctx}/product">Product</a>
            <a href="${ctx}/about">About Us</a>
            <a href="${ctx}/contact">Contact</a>
        </nav>
    </div>
</header>

<main class="farmer-apply-main">
    <div class="farmer-apply-page">
        <header class="vendor-form-page__intro">
            <h1 class="vendor-form-page__title">Apply to sell on Krishak</h1>
            <p class="vendor-form-page__subtitle">
                Tell us about your farm. After approval, you will receive a <strong>@krishak.np</strong> login to access the farmer dashboard.
            </p>
        </header>

        <c:if test="${param.ok eq '1'}">
            <p class="vendor-form-page__notice" role="status">
                Thank you! Your application was received. When an admin approves it, you will get a <strong>@krishak.np</strong> email and password to sign in.
            </p>
        </c:if>

        <c:if test="${applicationRejected}">
            <p class="vendor-form-page__notice vendor-form-page__notice--rejected" role="status">
                Your previous vendor application was <strong>not approved</strong>. You may update the form below and submit a new application.
            </p>
        </c:if>

        <c:if test="${not empty errors}">
            <div class="farmer-apply-errors" role="alert">
                <p class="farmer-apply-errors__title">Please fix the following:</p>
                <ul class="farmer-apply-errors__list">
                    <c:forEach var="err" items="${errors}">
                        <li><c:out value="${err}" /></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <div class="vendor-form-card">
            <form class="vendor-form"
                  method="post"
                  action="${ctx}/farmer/apply"
                  novalidate>
                <div class="vendor-form__grid">
                    <div class="vendor-field">
                        <label class="vendor-field__label" for="applicantName">Your name <span class="vendor-field__req" aria-hidden="true">*</span></label>
                        <input class="vendor-field__input" id="applicantName" name="applicantName" type="text" required
                               maxlength="120" autocomplete="name"
                               placeholder="Full name"
                               value="<c:out value='${empty applicantName ? param.applicantName : applicantName}' />" />
                    </div>
                    <div class="vendor-field">
                        <label class="vendor-field__label" for="farmName">Farm or business name <span class="vendor-field__req" aria-hidden="true">*</span></label>
                        <input class="vendor-field__input" id="farmName" name="farmName" type="text" required
                               maxlength="160" autocomplete="organization"
                               placeholder="e.g. Green Acres Organic"
                               value="<c:out value='${empty farmName ? param.farmName : farmName}' />" />
                    </div>

                    <div class="vendor-field">
                        <label class="vendor-field__label" for="email">Email <span class="vendor-field__req" aria-hidden="true">*</span></label>
                        <input class="vendor-field__input" id="email" name="email" type="email" required
                               autocomplete="email" placeholder="you@gmail.com (contact email)"
                               value="<c:out value='${empty email ? param.email : email}' />" />
                    </div>
                    <div class="vendor-field">
                        <label class="vendor-field__label" for="phone">Phone <span class="vendor-field__req" aria-hidden="true">*</span></label>
                        <input class="vendor-field__input" id="phone" name="phone" type="tel" required
                               autocomplete="tel" placeholder="10-digit mobile or with country code"
                               value="<c:out value='${empty phone ? param.phone : phone}' />" />
                    </div>

                    <div class="vendor-field vendor-field--full">
                        <label class="vendor-field__label" for="category">What you grow or sell</label>
                        <c:set var="cat" value="${not empty category ? category : param.category}" />
                        <select class="vendor-field__input vendor-field__select" id="category" name="category">
                            <option value="" ${empty cat ? 'selected="selected"' : ''}>Select (optional)</option>
                            <option value="vegetables" ${cat eq 'vegetables' ? 'selected="selected"' : ''}>Vegetables</option>
                            <option value="fruits" ${cat eq 'fruits' ? 'selected="selected"' : ''}>Fruits</option>
                            <option value="grains" ${cat eq 'grains' ? 'selected="selected"' : ''}>Grains</option>
                            <option value="dairy" ${cat eq 'dairy' ? 'selected="selected"' : ''}>Dairy</option>
                            <option value="mixed" ${cat eq 'mixed' ? 'selected="selected"' : ''}>Mixed / other</option>
                        </select>
                    </div>

                    <div class="vendor-field vendor-field--full">
                        <label class="vendor-field__label vendor-field__label--optional" for="about">Anything else we should know (optional)</label>
                        <textarea class="vendor-field__input vendor-field__textarea" id="about" name="about" rows="4" maxlength="2000"
                                  placeholder="Certifications, delivery areas, peak seasons…"><c:out value="${empty about ? param.about : about}" default="" /></textarea>
                    </div>
                </div>

                <p class="farmer-apply-footnote">
                    By submitting this form you agree that Krishak may contact you about your application.
                    Category and notes are for context only and are not shown on the admin list yet.
                </p>

                <div class="vendor-form__actions">
                    <a class="vendor-form__btn vendor-form__btn--cancel" href="${ctx}/">Cancel</a>
                    <button class="vendor-form__btn vendor-form__btn--submit" type="submit">Submit application</button>
                </div>
            </form>
        </div>
    </div>
</main>
<script>
(function () {
    var input = document.getElementById('email');
    if (!input) return;
    input.addEventListener('blur', function () {
        var value = this.value.trim();
        if (!value || !value.includes('@')) return;
        var url = new URL(window.location.href);
        if (url.searchParams.get('email') === value) return;
        url.searchParams.set('email', value);
        url.searchParams.delete('ok');
        window.location.search = url.search;
    });
})();
</script>
</body>
</html>
