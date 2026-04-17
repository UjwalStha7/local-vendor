function showError(element, message) {
  element.textContent = message;
  element.classList.add("show");
}

function clearError(element) {
  element.textContent = "";
  element.classList.remove("show");
}

function togglePassword(button) {
  const input = document.getElementById(button.dataset.target);
  const isPassword = input.type === "password";
  input.type = isPassword ? "text" : "password";
  button.setAttribute("aria-label", isPassword ? "Hide password" : "Show password");

  const img = button.querySelector("img");
  if (img) {
    img.src = isPassword ? "image/hide.png" : "image/visible.png";
  }
}

document.querySelectorAll(".password-toggle").forEach((button) => {
  button.addEventListener("click", () => togglePassword(button));
});

const loginForm = document.getElementById("login-form");
if (loginForm) {
  const loginError = document.getElementById("login-error");

  loginForm.addEventListener("submit", function (event) {
    event.preventDefault();
    clearError(loginError);

    const email = document.getElementById("login-email").value.trim();
    const password = document.getElementById("login-password").value.trim();

    if (!email || !password) {
      showError(loginError, "Please fill in your email and password.");
      return;
    }

    window.location.href = "shop.html";
  });

  loginForm.addEventListener("input", function () {
    clearError(loginError);
  });
}

const registerForm = document.getElementById("register-form");
if (registerForm) {
  const registerError = document.getElementById("register-error");

  registerForm.addEventListener("submit", function (event) {
    event.preventDefault();
    clearError(registerError);

    const name = document.getElementById("register-name").value.trim();
    const email = document.getElementById("register-email").value.trim();
    const password = document.getElementById("register-password").value.trim();
    const confirmPassword = document.getElementById("register-confirm-password").value.trim();

    if (!name || !email || !password || !confirmPassword) {
      showError(registerError, "Please complete all registration fields.");
      return;
    }

    if (password !== confirmPassword) {
      showError(registerError, "Passwords do not match.");
    }
  });

  registerForm.addEventListener("input", function () {
    clearError(registerError);
  });
}

