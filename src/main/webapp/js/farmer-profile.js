/**
 * Vendor Profile — live customer preview + logo fallback.
 */
(function () {
  var form = document.getElementById("vendorProfileForm");
  if (!form) return;

  var defaultBio =
    "Your trusted source for fresh, organic produce. We work directly with local farmers " +
    "to bring you the highest quality fruits and vegetables.";
  var defaultAddress = "1234 Farm Road, Green Valley, CA 94000";

  function bindImgFallback(img) {
    if (!img) return;
    var fallback = img.getAttribute("data-fallback");
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
    var shopName = val("shopName") || "FreshHarvest Farms";
    var bio = val("shopBio") || defaultBio;
    var email = val("email") || "";
    var phone = val("phone") || "";
    var address = val("address") || defaultAddress;
    var logoUrl = val("logoUrl");

    var nameEl = document.getElementById("previewShopName");
    var bioEl = document.getElementById("previewBio");
    var emailEl = document.getElementById("previewEmail");
    var phoneEl = document.getElementById("previewPhone");
    var addrEl = document.getElementById("previewAddress");
    var logoEl = document.getElementById("previewLogo");
    var logoPreview = document.getElementById("logoPreviewImg");

    if (nameEl) nameEl.textContent = shopName;
    if (bioEl) bioEl.textContent = bio;
    if (emailEl) emailEl.textContent = email;
    if (phoneEl) phoneEl.textContent = phone;
    if (addrEl) addrEl.textContent = address;

    if (logoUrl) {
      if (logoEl) logoEl.src = logoUrl;
      if (logoPreview) logoPreview.src = logoUrl;
    }
  }

  form.querySelectorAll("[data-preview-field]").forEach(function (field) {
    field.addEventListener("input", syncPreview);
    field.addEventListener("change", syncPreview);
  });

  if (form.getAttribute("data-preview") === "true") {
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      syncPreview();
      window.alert("Preview mode — sign in as a vendor to save your profile.");
    });
  }
})();
