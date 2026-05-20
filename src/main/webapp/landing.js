/**
 * Home page — featured products from database
 */
(function () {
  const CART_KEY = "cartItems";
  const FEATURED_LIMIT = 6;

  function getContextPath() {
    const base = document.querySelector("base");
    if (base && base.href) {
      try {
        const url = new URL(base.href);
        let path = url.pathname;
        if (path.endsWith("/")) path = path.slice(0, -1);
        return path;
      } catch {
        // ignore
      }
    }
    return "";
  }

  const CTX = getContextPath();

  function $(id) {
    return document.getElementById(id);
  }

  function formatMoney(value) {
    return `Rs.${Number(value).toFixed(0)}`;
  }

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

  function writeCart(items) {
    try {
      localStorage.setItem(CART_KEY, JSON.stringify(items));
    } catch {
      // ignore
    }
  }

  function upsertCartItem(item) {
    const cart = readCart();
    const key = String(item.productId);
    const idx = cart.findIndex((x) => x && String(x.productId || x.id) === key);
    if (idx >= 0) {
      cart[idx] = {
        ...cart[idx],
        ...item,
        qty: Math.max(1, Number(cart[idx].qty || 1) + Number(item.qty || 1)),
      };
    } else {
      cart.push(item);
    }
    writeCart(cart);
  }

  function updateCartBadge() {
    const badge = $("cart-badge");
    if (!badge) return;
    const count = readCart().reduce((sum, x) => sum + Math.max(1, Number(x.qty || 1)), 0);
    badge.textContent = String(count);
  }

  function showToast(message) {
    const toast = $("toast");
    if (!toast) return;
    toast.textContent = message;
    toast.classList.add("show");
    clearTimeout(showToast._timer);
    showToast._timer = setTimeout(() => toast.classList.remove("show"), 2200);
  }

  function stockLabel(p) {
    if (p.outOfStock) return "Out of Stock";
    if (p.limited) return "Limited";
    return "In Stock";
  }

  function renderFeatured(products) {
    const grid = $("product-grid");
    if (!grid) return;

    if (!products.length) {
      grid.innerHTML =
        '<p class="section-lead" style="grid-column:1/-1;text-align:center;">No products available yet. Vendors are adding fresh produce soon — check back or <a href="' +
        CTX +
        '/product">browse the shop</a>.</p>';
      return;
    }

    grid.innerHTML = products
      .slice(0, FEATURED_LIMIT)
      .map((p) => {
        const disabled = p.outOfStock ? "disabled" : "";
        const badgeClass = p.limited ? "product-card__badge product-card__badge--limited" : "product-card__badge product-card__badge--in";
        return `
        <article class="product-card" data-id="${p.id}">
          <div class="product-card__media">
            <img src="${p.image}" alt="${escapeHtml(p.name)}" loading="lazy" />
            <span class="${badgeClass}">${stockLabel(p)}</span>
          </div>
          <div class="product-card__body">
            <h3 class="product-card__title">${escapeHtml(p.name)}</h3>
            <p class="product-card__vendor">by ${escapeHtml(p.vendor)}</p>
            <p class="product-card__price">${formatMoney(p.price)}</p>
            <p class="product-card__unit">per ${escapeHtml(p.unit)}</p>
            <button type="button" class="add-cart" data-action="add" aria-label="Add ${escapeHtml(p.name)} to cart" ${disabled}>
              <img src="image/white_cart.png" alt="" />
            </button>
          </div>
        </article>`;
      })
      .join("");
  }

  function escapeHtml(text) {
    return String(text || "")
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;");
  }

  async function loadFeatured() {
    const url =
      CTX +
      "/api/products?limit=" +
      FEATURED_LIMIT +
      "&inStockOnly=1&sort=featured";
    const res = await fetch(url, { headers: { Accept: "application/json" } });
    if (!res.ok) return [];
    const data = await res.json();
    return Array.isArray(data.products) ? data.products : [];
  }

  let featuredProducts = [];

  async function init() {
    updateCartBadge();
    window.addEventListener("storage", updateCartBadge);

    try {
      featuredProducts = await loadFeatured();
      renderFeatured(featuredProducts);
    } catch (e) {
      console.error(e);
      const grid = $("product-grid");
      if (grid) {
        grid.innerHTML =
          '<p class="section-lead" style="grid-column:1/-1;text-align:center;">Could not load featured products.</p>';
      }
    }

    const grid = $("product-grid");
    if (grid) {
      grid.addEventListener("click", (e) => {
        const btn = e.target.closest('button[data-action="add"]');
        if (!btn || btn.disabled) return;
        const card = btn.closest("[data-id]");
        if (!card) return;
        const id = Number(card.getAttribute("data-id"));
        const p = featuredProducts.find((x) => x.id === id);
        if (!p || p.outOfStock) return;

        upsertCartItem({
          productId: p.id,
          id: String(p.id),
          name: p.name,
          unitPrice: p.price,
          unitLabel: `/${p.unit}`,
          qty: 1,
          image: p.image,
        });
        updateCartBadge();
        showToast(p.name + " added to cart");
      });
    }
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
