/**
 * Customer product catalog — loads from database via /api/products
 */
const PAGE_SIZE = 9;
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

function selectedValues(name) {
  return Array.from(document.querySelectorAll(`input[name="${name}"]:checked`)).map((el) => el.value);
}

function readState() {
  const q = ($("q")?.value || "").trim();
  const cats = selectedValues("cat").filter((c) => c !== "all");
  const vendors = selectedValues("vendor");
  const minRaw = ($("priceMin")?.value || "").trim();
  const maxRaw = ($("priceMax")?.value || "").trim();
  const min = minRaw === "" ? "" : minRaw;
  const max = maxRaw === "" ? "" : maxRaw;
  const avail = document.querySelector('input[name="avail"]:checked')?.value || "all";
  const sortBy = $("sortBy")?.value || "featured";
  return { q, cats, vendors, min, max, avail, sortBy };
}

function buildQuery(s) {
  const params = new URLSearchParams();
  if (s.q) params.set("q", s.q);
  if (s.cats.length) params.set("cat", s.cats.join(","));
  if (s.vendors.length) params.set("vendor", s.vendors.join(","));
  if (s.min !== "") params.set("min", s.min);
  if (s.max !== "") params.set("max", s.max);
  if (s.avail && s.avail !== "all") params.set("avail", s.avail);
  if (s.sortBy) params.set("sort", s.sortBy);
  return params.toString();
}

function renderStarsInline() {
  const full = "image/yellow_rating.png";
  const empty = "image/rating.png";
  const stars = new Array(5)
    .fill(0)
    .map((_, i) => `<img src="${i < 5 ? full : empty}" alt="" aria-hidden="true" />`)
    .join("");
  return `<span class="rating" aria-label="Rating 5.0">${stars} <span class="rating-num">(5.0)</span></span>`;
}

function productHref(p) {
  return `productId?id=${encodeURIComponent(p.id)}`;
}

let page = 0;
let products = [];

async function loadProducts() {
  const s = readState();
  const qs = buildQuery(s);
  const url = `${CTX}/api/products${qs ? `?${qs}` : ""}`;
  const res = await fetch(url, { headers: { Accept: "application/json" } });
  if (!res.ok) {
    products = [];
    return;
  }
  const data = await res.json();
  products = Array.isArray(data.products) ? data.products : [];
}

async function loadFilterMeta() {
  const res = await fetch(`${CTX}/api/catalog/meta`, { headers: { Accept: "application/json" } });
  if (!res.ok) return;
  const data = await res.json();

  const catField = document.querySelector('.field:has(input[name="cat"])') || document.querySelector('input[name="cat"]')?.closest(".field");
  if (catField && Array.isArray(data.categories) && data.categories.length > 0) {
    const existing = catField.querySelectorAll('input[name="cat"]:not([value="all"])');
    existing.forEach((el) => el.closest("label")?.remove());
    data.categories.forEach((cat) => {
      const label = document.createElement("label");
      label.className = "check";
      label.innerHTML = `<input type="checkbox" name="cat" value="${cat.replace(/"/g, "&quot;")}" /><span>${cat}</span>`;
      catField.appendChild(label);
    });
  }

  const vendorField = $("vendorFilters");
  const vendorHint = $("vendorFiltersHint");
  if (vendorField && Array.isArray(data.vendors)) {
    if (vendorHint) vendorHint.remove();
    vendorField.querySelectorAll('input[name="vendor"]').forEach((el) => el.closest("label")?.remove());
    if (data.vendors.length === 0) {
      const empty = document.createElement("p");
      empty.className = "field-hint";
      empty.textContent = "No vendors with products yet.";
      vendorField.appendChild(empty);
    } else {
      data.vendors.forEach((v) => {
        const label = document.createElement("label");
        label.className = "check";
        label.innerHTML = `<input type="checkbox" name="vendor" value="${v.slug}" /><span>${v.name}</span>`;
        vendorField.appendChild(label);
      });
    }
  }
}

function render() {
  $("foundCount").textContent = String(products.length);

  const maxPage = Math.max(0, Math.ceil(products.length / PAGE_SIZE) - 1);
  page = Math.max(0, Math.min(page, maxPage));

  const slice = products.slice(page * PAGE_SIZE, page * PAGE_SIZE + PAGE_SIZE);
  const pageBtn = $("pageBtn");
  if (pageBtn) pageBtn.textContent = String(page + 1);

  const grid = $("grid");
  if (slice.length === 0) {
    grid.innerHTML = `<div style="padding:18px;color:rgba(15,23,42,0.65);font-weight:600;">No products match your filters. Try adjusting search or filters.</div>`;
  } else {
    grid.innerHTML = slice
      .map((p) => {
        let badgeText = "In Stock";
        let badgeClass = "green";
        if (p.outOfStock) {
          badgeText = "Out of Stock";
          badgeClass = "";
        } else if (p.limited) {
          badgeText = "Limited";
          badgeClass = "";
        }
        const disabled = p.outOfStock ? "disabled" : "";
        return `
          <article class="card" data-id="${p.id}">
            <div class="card-media">
              <a class="open" href="${productHref(p)}" aria-label="Open ${p.name}">
                <img src="${p.image}" alt="${p.name}" />
              </a>
              <span class="badge ${badgeClass}">${badgeText}</span>
            </div>
            <div class="card-body">
              <div class="card-title">
                <a class="open" href="${productHref(p)}" style="text-decoration:none;color:inherit">${p.name}</a>
              </div>
              <p class="vendor">by ${p.vendor}</p>
              <div class="card-rating">${renderStarsInline()}</div>
              <div class="card-meta">
                <span class="price">${formatMoney(p.price)}</span>
                <span class="unit">/${p.unit}</span>
              </div>
              <div class="card-actions">
                <button class="btn btn-primary" type="button" data-action="add" ${disabled}>
                  <img src="image/white_cart.png" alt="" aria-hidden="true" />
                  Add to Cart
                </button>
              </div>
            </div>
          </article>
        `;
      })
      .join("");
  }

  $("prevBtn").disabled = page <= 0;
  $("nextBtn").disabled = page >= maxPage;
}

async function refresh() {
  await loadProducts();
  render();
}

function wire() {
  updateCartBadge();
  window.addEventListener("storage", updateCartBadge);

  let searchTimer;
  $("q").addEventListener("input", () => {
    clearTimeout(searchTimer);
    searchTimer = setTimeout(() => {
      page = 0;
      refresh();
    }, 300);
  });

  $("sortBy").addEventListener("change", () => {
    page = 0;
    refresh();
  });

  $("applyBtn").addEventListener("click", () => {
    page = 0;
    refresh();
  });

  document.querySelectorAll('input[name="avail"]').forEach((el) => {
    el.addEventListener("change", () => {
      page = 0;
      refresh();
    });
  });

  $("prevBtn").addEventListener("click", () => {
    page = Math.max(0, page - 1);
    render();
  });

  $("nextBtn").addEventListener("click", () => {
    page = page + 1;
    render();
  });

  $("grid").addEventListener("click", (e) => {
    const btn = e.target.closest('button[data-action="add"]');
    if (!btn || btn.disabled) return;
    const card = btn.closest("[data-id]");
    if (!card) return;
    const id = Number(card.getAttribute("data-id"));
    const p = products.find((x) => x.id === id);
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
    btn.textContent = "Added!";
    setTimeout(() => {
      btn.innerHTML = '<img src="image/white_cart.png" alt="" aria-hidden="true" /> Add to Cart';
    }, 900);
  });

  document.addEventListener("change", (e) => {
    if (e.target.matches('input[name="cat"]')) {
      const all = document.querySelector('input[name="cat"][value="all"]');
      const others = Array.from(document.querySelectorAll('input[name="cat"]')).filter((x) => x !== all);
      if (e.target.value === "all" && e.target.checked) others.forEach((x) => (x.checked = false));
      if (e.target.value !== "all" && e.target.checked) all.checked = false;
      if (e.target.value !== "all" && !others.some((x) => x.checked)) all.checked = true;
      page = 0;
      refresh();
    }
  });
}

(async function init() {
  try {
    await loadFilterMeta();
    wire();
    await refresh();
  } catch (err) {
    console.error(err);
    $("grid").innerHTML = `<div style="padding:18px;color:#b91c1c;">Could not load products. Please refresh the page.</div>`;
  }
})();
