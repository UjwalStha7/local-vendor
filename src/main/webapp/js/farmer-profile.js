/**
 * Vendor Profile — live customer preview, logo file preview, logo fallback.
 */
(function () {
  var form = document.getElementById("vendorProfileForm");
  if (!form) return;

  var ctx = form.getAttribute("data-ctx") || "";
  var defaultLogo = ctx + "/image/fresh_apple.png";

  function bindImgFallback(img) {
    if (!img) return;
    var fallback = img.getAttribute("data-fallback") || defaultLogo;
    img.addEventListener("error", function onErr() {
      if (fallback && img.src !== fallback) {
        img.src = fallback;
        img.removeEventListener("error", onErr);
      }
    });
  }

  bindImgFallback(document.getElementById("logoPreviewImg"));
  bindImgFallback(document.getElementById("previewLogo"));

  function val(id) {
    var el = document.getElementById(id);
    return el ? el.value.trim() : "";
  }

  function syncPreview() {
    var shopName = val("shopName") || "Your shop";
    var bio = val("shopBio");
    var email = val("email") || "";
    var phone = val("phone") || "";
    var address = val("address") || "";

    var nameEl = document.getElementById("previewShopName");
    var bioEl = document.getElementById("previewBio");
    var emailEl = document.getElementById("previewEmail");
    var phoneEl = document.getElementById("previewPhone");
    var addrEl = document.getElementById("previewAddress");

    if (nameEl) nameEl.textContent = shopName;
    if (bioEl) {
      if (bio) {
        bioEl.textContent = bio;
        bioEl.hidden = false;
      } else {
        bioEl.textContent = "";
        bioEl.hidden = true;
      }
    }
    if (emailEl) emailEl.textContent = email;
    if (phoneEl) phoneEl.textContent = phone;
    if (addrEl) addrEl.textContent = address;
  }

  form.querySelectorAll("[data-preview-field]").forEach(function (field) {
    field.addEventListener("input", syncPreview);
    field.addEventListener("change", syncPreview);
  });

  var logoInput = form.querySelector("[data-logo-file-input]");
  if (logoInput) {
    logoInput.addEventListener("change", function () {
      var file = logoInput.files && logoInput.files[0];
      if (!file) return;
      var url = URL.createObjectURL(file);
      var logoPreview = document.getElementById("logoPreviewImg");
      var previewLogo = document.getElementById("previewLogo");
      if (logoPreview) logoPreview.src = url;
      if (previewLogo) previewLogo.src = url;
    });
  }

  if (form.getAttribute("data-preview") === "true") {
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      syncPreview();
      window.alert("Preview mode — sign in as a vendor to save your profile.");
    });
  }
})();
