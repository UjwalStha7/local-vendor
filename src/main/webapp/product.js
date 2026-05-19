/**
 * Product detail page — loads product by id from /api/products?id=
 */
const CART_KEY = "cartItems";

function $(id) {
  return document.getElementById(id);
}

function getContextPath() {
  const base = document.querySelector("base");
  if (base && base.href) {
    try {
      const url = new URL(base.href);
      return url.pathname.endsWith("/") ? url.pathname.slice(0, -1) : url.pathname.replace(/\/$/, "");
    } catch {
      // ignore
    }
  }
  return "";
}

const CTX = getContextPath();

function formatMoney(value) {
  return `Rs.${Number(value).toFixed(0)}`;
}

function getProductIdFromUrl() {
  const url = new URL(window.location.href);
  const raw = url.searchParams.get("id");
  if (!raw) return 0;
  const id = Number(raw);
  return Number.isFinite(id) && id > 0 ? id : 0;
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

function cartCount() {
  return readCart().reduce((sum, x) => sum + Math.max(1, Number(x.qty || 1)), 0);
}

function updateCartBadge() {
  const badge = $("cart-badge");
  if (!badge) return;
  badge.textContent = String(cartCount());
}

function renderStars() {
  const starsEl = document.querySelector(".stars");
  if (!starsEl) return;
  const html = new Array(5)
    .fill(0)
    .map((_, i) => {
      const src = i < 5 ? "image/yellow_rating.png" : "image/rating.png";
      return `<img class="star-img" src="${src}" alt="" aria-hidden="true" />`;
    })
    .join("");
  starsEl.innerHTML = html;
}

function stockLabel(p) {
  if (p.outOfStock) return "Out of Stock";
  if (p.limited) return "Limited Stock";
  return "In Stock";
}

function showToast(message) {
  const toast = $("toast");
  if (!toast) return;
  toast.textContent = message;
  window.clearTimeout(showToast._t);
  showToast._t = window.setTimeout(() => {
    toast.textContent = "";
  }, 1600);
}
showToast._t = 0;

function showError() {
  $("product-main").hidden = true;
  $("product-error").hidden = false;
}

function showProduct(p) {
  $("product-error").hidden = true;
  $("product-main").hidden = false;

  document.title = `${p.name} — Krishak`;

  const hero = $("hero-img");
  hero.src = p.image;
  hero.alt = p.name;

  $("category").textContent = p.category || "";
  $("title").textContent = p.name;
  $("price").textContent = formatMoney(p.price);
  $("unit").textContent = p.unit ? `/ ${p.unit}` : "";
  $("desc").textContent =
    p.description && p.description.trim()
      ? p.description.trim()
      : "Fresh produce from a local vendor on Krishak.";

  $("vendor").textContent = p.vendor || "—";
  $("unit-spec").textContent = p.unit || "—";
  $("type").textContent = p.category || "—";
  $("stock-spec").textContent = p.stock > 0 ? `${p.stock} left` : "None";

  const pill = $("stock-pill");
  pill.textContent = stockLabel(p);
  pill.classList.toggle("out", !!p.outOfStock);

  renderStars();

  let qty = 1;
  const qtyValue = $("qty-value");
  const setQty = (next) => {
    qty = Math.max(1, Math.min(99, next));
    qtyValue.textContent = String(qty);
  };

  $("qty-dec").onclick = () => setQty(qty - 1);
  $("qty-inc").onclick = () => setQty(qty + 1);

  const addBtn = $("add-btn");
  addBtn.disabled = !!p.outOfStock;
  addBtn.style.opacity = p.outOfStock ? "0.6" : "1";
  addBtn.style.cursor = p.outOfStock ? "not-allowed" : "pointer";

  addBtn.onclick = () => {
    if (p.outOfStock) return;
    upsertCartItem({
      productId: p.id,
      id: String(p.id),
      name: p.name,
      unitPrice: p.price,
      unitLabel: p.unit ? `/${p.unit}` : "",
      qty,
      image: p.image,
    });
    updateCartBadge();
    showToast("Added to cart");
    const label = addBtn.innerHTML;
    addBtn.textContent = "Added!";
    window.setTimeout(() => {
      addBtn.innerHTML = label;
    }, 900);
  };
}

async function loadProduct() {
  const id = getProductIdFromUrl();
  if (id <= 0) {
    showError();
    return;
  }

  const res = await fetch(`${CTX}/api/products?id=${encodeURIComponent(id)}`, {
    headers: { Accept: "application/json" },
  });

  if (!res.ok) {
    showError();
    return;
  }

  const data = await res.json();
  if (!data.product) {
    showError();
    return;
  }

  showProduct(data.product);
}

(async function init() {
  updateCartBadge();
  window.addEventListener("storage", updateCartBadge);
  try {
    await loadProduct();
  } catch (err) {
    console.error(err);
    showError();
  }
})();
