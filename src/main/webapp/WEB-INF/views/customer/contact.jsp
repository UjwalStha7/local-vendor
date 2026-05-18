<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Contact Us — Krishak</title>
  <base href="${pageContext.request.contextPath}/" />
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="landing.css" />
  <link rel="stylesheet" href="contact.css" />
</head>
<body>
  <header class="site-header" id="top">
    <div class="header-inner">
      <a class="logo" href="${pageContext.request.contextPath}/index.html">Krishak</a>
      <nav class="main-nav" aria-label="Primary">
        <a href="${pageContext.request.contextPath}/index.html#top">Home</a>
        <a href="${pageContext.request.contextPath}/shop">Product</a>
        <a href="${pageContext.request.contextPath}/index.html#about">About Us</a>
        <a href="${pageContext.request.contextPath}/contact" class="is-active" aria-current="page">Contact</a>
      </nav>
      <a class="cart-link" href="${pageContext.request.contextPath}/cart.html" aria-label="Shopping cart">
        <img src="image/cart.png" alt="" width="24" height="24" />
        <span id="cart-badge" class="cart-badge" aria-live="polite">0</span>
      </a>
    </div>
  </header>

  <section class="contact-hero" aria-labelledby="contact-hero-heading">
    <h1 id="contact-hero-heading">Get In Touch</h1>
    <p>
      Have questions or feedback? We'd love to hear from you. Reach out to us and we'll respond as soon as possible.
    </p>
  </section>

  <main class="contact-layout" aria-label="Contact Krishak">
    <div class="contact-panel">
      <h2>Send Us a Message</h2>

      <c:if test="${not empty messageSent}">
        <p class="contact-alert contact-alert--ok" role="status">Thank you — your message has been received. We'll get back to you soon.</p>
      </c:if>
      <c:if test="${not empty error}">
        <p class="contact-alert contact-alert--err" role="alert"><c:out value="${error}" /></p>
      </c:if>

      <form class="contact-form" method="post" action="${pageContext.request.contextPath}/contact">
        <div class="field">
          <label for="name">Name</label>
          <input id="name" name="name" type="text" required maxlength="200" placeholder="Your full name"
                 value="<c:out value='${fieldName}' />" />
        </div>
        <div class="field">
          <label for="email">Email</label>
          <input id="email" name="email" type="email" required maxlength="120" placeholder="your.email@example.com"
                 value="<c:out value='${fieldEmail}' />" />
        </div>
        <div class="field">
          <label for="subject">Subject</label>
          <input id="subject" name="subject" type="text" required maxlength="300" placeholder="How can we help?"
                 value="<c:out value='${fieldSubject}' />" />
        </div>
        <div class="field">
          <label for="message">Message</label>
          <textarea id="message" name="message" required maxlength="4000" placeholder="Tell us more about your inquiry..."><c:out value="${fieldMessage}" /></textarea>
        </div>
        <button type="submit" class="btn-send">Send Message</button>
      </form>
    </div>

    <div class="contact-panel">
      <h2>Contact Information</h2>

      <div class="contact-info-block">
        <div class="contact-info-icon" aria-hidden="true">
          <img src="image/phone.png" alt="" />
        </div>
        <div>
          <h3>Phone</h3>
          <p><a href="tel:+9779876543210">+977 9876543210</a></p>
          <span class="muted">Mon–Sat, 8:00 AM – 8:00 PM</span>
        </div>
      </div>

      <div class="contact-info-block">
        <div class="contact-info-icon" aria-hidden="true">
          <img src="image/green_email.png" alt="" />
        </div>
        <div>
          <h3>Email</h3>
          <p><a href="mailto:support@krishak.com">support@krishak.com</a></p>
          <span class="muted">We'll respond within 24 hours</span>
        </div>
      </div>

      <div class="contact-info-block">
        <div class="contact-info-icon" aria-hidden="true">
          <img src="image/location.png" alt="" />
        </div>
        <div>
          <h3>Address</h3>
          <p>12 Matepani, Pokhara, Gandaki, 33700, Nepal</p>
        </div>
      </div>

      <div class="contact-map" role="img" aria-label="Map location placeholder">Map Location</div>
    </div>
  </main>

  <footer class="site-footer">
    <div class="footer-grid container">
      <div class="footer-col" id="about">
        <h3>About Krishak</h3>
        <p>
          Connecting farmers directly with consumers, bringing fresh, organic produce straight from the farm to your table.
        </p>
      </div>
      <div class="footer-col">
        <h3>Quick Links</h3>
        <ul class="footer-links">
          <li><a href="${pageContext.request.contextPath}/index.html#top">Home</a></li>
          <li><a href="${pageContext.request.contextPath}/shop">Products</a></li>
          <li><a href="${pageContext.request.contextPath}/index.html#about">About Us</a></li>
          <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
        </ul>
      </div>
      <div class="footer-col">
        <h3 class="footer-title-link"><a href="${pageContext.request.contextPath}/contact">Contact Us</a></h3>
        <ul class="footer-contact">
          <li>
            <img src="image/phone.png" alt="" width="18" height="18" />
            <a href="tel:+9779876543210">+977 9876543210</a>
          </li>
          <li>
            <img src="image/green_email.png" alt="" width="18" height="18" />
            <a href="mailto:support@krishak.com">support@krishak.com</a>
          </li>
          <li>
            <img src="image/location.png" alt="" width="18" height="18" />
            <span>12 Matepani, Pokhara, Gandaki 33700</span>
          </li>
        </ul>
      </div>
      <div class="footer-col">
        <h3>Follow Us</h3>
        <div class="social-row" aria-label="Social media">
          <a class="social-btn" href="#" aria-label="Facebook"><img src="image/facebook.png" alt="" /></a>
          <a class="social-btn" href="#" aria-label="Twitter"><img src="image/bird.png" alt="" /></a>
          <a class="social-btn" href="#" aria-label="Instagram"><img src="image/instagram.png" alt="" /></a>
          <a class="social-btn" href="#" aria-label="LinkedIn"><img src="image/linkedIn.png" alt="" /></a>
        </div>
      </div>
    </div>

    <div class="footer-bar">
      <div class="container footer-bar-inner">
        <p>© 2026 Krishak. All rights reserved. | <a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a></p>
      </div>
    </div>
  </footer>
</body>
</html>
