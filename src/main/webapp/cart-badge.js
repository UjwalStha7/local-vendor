/**
 * Sync navbar cart badge from localStorage on every customer page.
 */
(function () {
  const CART_KEY = "cartItems";

  function readCart() {
    try {
      const raw = localStorage.getItem(CART_KEY);
      if (!raw) return [];
      const parsed = JSON.parse(raw);
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      return [];
    }
  }

  function cartCount() {
    return readCart().reduce((sum, x) => sum + Math.max(1, Number(x.qty || 1)), 0);
  }

  function updateCartBadge() {
    const badge = document.getElementById("cart-badge");
    if (!badge) return;
    badge.textContent = String(cartCount());
  }

  window.updateCartBadge = updateCartBadge;

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", updateCartBadge);
  } else {
    updateCartBadge();
  }

  window.addEventListener("storage", updateCartBadge);
})();
