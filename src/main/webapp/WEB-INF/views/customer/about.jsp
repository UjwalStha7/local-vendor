<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>About Us — Krishak</title>
  <base href="${pageContext.request.contextPath}/" />
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="landing.css" />
  <link rel="stylesheet" href="about.css" />
</head>
<body class="page-about">
  <jsp:include page="/WEB-INF/views/customer/navbar.jsp">
    <jsp:param name="current" value="about" />
  </jsp:include>

  <main>
    <section class="about-hero" aria-labelledby="about-heading">
      <h1 id="about-heading">About Krishak</h1>
      <p>
        Connecting farmers directly with consumers, bringing fresh, organic produce straight from the farm to your table.
      </p>
    </section>

    <section class="about-story" aria-labelledby="story-heading">
      <div class="container">
        <h2 id="story-heading">Our Story</h2>
        <p>
          Krishak was born from a simple vision: to bridge the gap between hardworking farmers and families who deserve
          honest, nutritious food. We believe everyone should know where their produce comes from—and that growers should
          be paid fairly for the care they put into the land.
        </p>
        <p>
          Our journey began in the fertile valleys around Pokhara, partnering with local organic farmers who share our
          values for sustainability and quality. What started as a small network has grown into a trusted marketplace
          built on transparency, freshness, and respect for tradition.
        </p>
        <p>
          Today we are proud to serve thousands of households with premium organic fruits and vegetables, delivered with
          the same attention we would want for our own families. Thank you for letting us be part of your table.
        </p>
      </div>
    </section>

    <section class="about-team" aria-labelledby="team-heading">
      <h2 id="team-heading">Meet Our Team</h2>
      <div class="team-grid">
        <article class="team-card">
          <div class="team-photo" aria-hidden="true">
            <span class="team-photo__fallback">US</span>
            <img class="team-photo__img" src="image/team/ujwal.png" alt="" width="120" height="120" loading="lazy" decoding="async"
                 onload="this.classList.add('is-loaded')" onerror="this.classList.add('is-broken')" />
          </div>
          <h3>Ujwal Shrestha</h3>
          <p>Founder &amp; CEO</p>
        </article>
        <article class="team-card">
          <div class="team-photo" aria-hidden="true">
            <span class="team-photo__fallback">JG</span>
            <img class="team-photo__img" src="image/team/jon.png" alt="" width="120" height="120" loading="lazy" decoding="async"
                 onload="this.classList.add('is-loaded')" onerror="this.classList.add('is-broken')" />
          </div>
          <h3>Jon Gurung</h3>
          <p>Farm Manager</p>
        </article>
        <article class="team-card">
          <div class="team-photo" aria-hidden="true">
            <span class="team-photo__fallback">KS</span>
            <img class="team-photo__img" src="image/team/kushal.png" alt="" width="120" height="120" loading="lazy" decoding="async"
                 onload="this.classList.add('is-loaded')" onerror="this.classList.add('is-broken')" />
          </div>
          <h3>Kushal Shrestha</h3>
          <p>Logistics Head</p>
        </article>
        <article class="team-card">
          <div class="team-photo" aria-hidden="true">
            <span class="team-photo__fallback">IA</span>
            <img class="team-photo__img" src="image/team/ishant.png" alt="" width="120" height="120" loading="lazy" decoding="async"
                 onload="this.classList.add('is-loaded')" onerror="this.classList.add('is-broken')" />
          </div>
          <h3>Ishant Kumar Acharya</h3>
          <p>Quality Lead</p>
        </article>
        <article class="team-card">
          <div class="team-photo" aria-hidden="true">
            <span class="team-photo__fallback">SG</span>
            <img class="team-photo__img" src="image/team/shirsh.png" alt="" width="120" height="120" loading="lazy" decoding="async"
                 onload="this.classList.add('is-loaded')" onerror="this.classList.add('is-broken')" />
          </div>
          <h3>Shirsh Gurung</h3>
          <p>Customer Relations</p>
        </article>
        <article class="team-card">
          <div class="team-photo" aria-hidden="true">
            <span class="team-photo__fallback">DS</span>
            <img class="team-photo__img" src="image/team/darshan.png" alt="" width="120" height="120" loading="lazy" decoding="async"
                 onload="this.classList.add('is-loaded')" onerror="this.classList.add('is-broken')" />
          </div>
          <h3>Darshan Shrestha</h3>
          <p>Operations Manager</p>
        </article>
      </div>
    </section>
  </main>

  <footer class="site-footer">
    <div class="footer-grid container">
      <div class="footer-col">
        <h3>About Krishak</h3>
        <p>
          Connecting farmers directly with consumers, bringing fresh, organic produce straight from the farm to your table.
        </p>
      </div>
      <div class="footer-col">
        <h3>Quick Links</h3>
        <ul class="footer-links">
          <jsp:include page="/WEB-INF/views/customer/footer-quicklinks.jsp" />
        </ul>
      </div>
      <div class="footer-col" id="contact">
        <h3>Contact Us</h3>
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
            <span>12 Matepani, Pokhara, Gandaki 33700, Nepal</span>
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
        <p>© 2026 Krishak. All rights reserved.</p>
      </div>
    </div>
  </footer>
</body>
</html>
