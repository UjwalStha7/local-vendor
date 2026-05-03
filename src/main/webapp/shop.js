/** @typedef {{id:string,name:string,category:"Fruits"|"Vegetables",vendor:string,price:number,unit:string,image:string,rating:number,inStock:boolean,limited:boolean}} Product */

/** @type {Product[]} */
const PRODUCTS = [
  {
    id: "fresh-tomatoes",
    name: "Fresh Tomatoes",
    category: "Vegetables",
    vendor: "Green Valley Farm",
    price: 45,
    unit: "kg",
    image: "image/fresh_tomato.png",
    rating: 5.0,
    inStock: true,
    limited: false,
  },
  {
    id: "organic-carrots",
    name: "Organic Carrots",
    category: "Vegetables",
    vendor: "Organic Harvest",
    price: 60,
    unit: "kg",
    image: "image/organic_carrot.png",
    rating: 5.0,
    inStock: true,
    limited: false,
  },
  {
    id: "fresh-strawberries",
    name: "Fresh Strawberries",
    category: "Fruits",
    vendor: "Fresh Fields",
    price: 120,
    unit: "kg",
    image: "image/fresh_strawberries.png",
    rating: 5.0,
    inStock: true,
    limited: true,
  },
  {
    id: "organic-carrots-2",
    name: "Organic Carrots",
    category: "Vegetables",
    vendor: "Organic Harvest",
    price: 60,
    unit: "kg",
    image: "image/organic_carrot.png",
    rating: 5.0,
    inStock: true,
    limited: true,
  },
  {
    id: "fresh-bell-peppers",
    name: "Bell Peppers",
    category: "Vegetables",
    vendor: "Nature's Best",
    price: 80,
    unit: "kg",
    image: "image/fresh_bell_pepper.png",
    rating: 5.0,
    inStock: true,
    limited: false,
  },
  {
    id: "fresh-apples",
    name: "Fresh Apples",
    category: "Fruits",
    vendor: "Green Valley Farm",
    price: 150,
    unit: "kg",
    image: "image/fresh_apple.png",
    rating: 5.0,
    inStock: true,
    limited: false,
  },
  {
    id: "mixed-vegetables",
    name: "Mixed Vegetables",
    category: "Vegetables",
    vendor: "Organic Harvest",
    price: 90,
    unit: "kg",
    image: "image/mix_vegetables.png",
    rating: 5.0,
    inStock: true,
    limited: false,
  },
  {
    id: "fresh-oranges",
    name: "Fresh Oranges",
    category: "Fruits",
    vendor: "Fresh Fields",
    price: 100,
    unit: "kg",
    image: "image/fresh_orange.png",
    rating: 5.0,
    inStock: true,
    limited: true,
  },
  {
    id: "green-cucumbers",
    name: "Green Cucumbers",
    category: "Vegetables",
    vendor: "Nature's Best",
    price: 40,
    unit: "kg",
    image: "image/green_cucumber.png",
    rating: 5.0,
    inStock: true,
    limited: false,
  },
  {
    id: "fresh-citrus-mix",
    name: "Fresh Citrus Mix",
    category: "Fruits",
    vendor: "Green Valley Farm",
    price: 110,
    unit: "kg",
    image: "image/fresh_citrus_mix.png",
    rating: 5.0,
    inStock: true,
    limited: false,
  },
];

const PAGE_SIZE = 9;

function $(id) {
  return document.getElementById(id);
}

function formatMoney(value) {
  return `Rs.${Number(value).toFixed(0)}`;
}

function readCart() {
  try {
    const raw = localStorage.getItem("cartItems");
    if (!raw) return [];
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

function writeCart(items) {
  try {
    localStorage.setItem("cartItems", JSON.stringify(items));
  } catch {
    // ignore
  }
}

function upsertCartItem(item) {
  const cart = readCart();
  const idx = cart.findIndex((x) => x && x.id === item.id);
  if (idx >= 0) cart[idx] = { ...cart[idx], qty: Math.max(1, Number(cart[idx].qty || 1) + item.qty) };
  else cart.push(item);
  writeCart(cart);
}

function cartCount() {
  return readCart().reduce((sum, x) => sum + Math.max(1, Number(x.qty || 1)), 0);
}

function updateCartBadge() {
  const badge = $("cart-badge");
  if (!badge) return;
  const count = cartCount();
  badge.textContent = String(count);
}

function selectedValues(name) {
  return Array.from(document.querySelectorAll(`input[name="${name}"]:checked`)).map((el) => el.value);
}

function vendorSlug(vendor) {
  return vendor.toLowerCase().replace(/\s+/g, "-");
}

function readState() {
  const q = ($("q")?.value || "").trim().toLowerCase();
  const cats = selectedValues("cat");
  const vendors = selectedValues("vendor");
  const minRaw = ($("priceMin")?.value || "").trim();
  const maxRaw = ($("priceMax")?.value || "").trim();
  const min = minRaw === "" ? NaN : Number(minRaw);
  const max = maxRaw === "" ? NaN : Number(maxRaw);
  const avail = document.querySelector('input[name="avail"]:checked')?.value || "all";
  const sortBy = $("sortBy")?.value || "featured";

  return {
    q,
    cats,
    vendors,
    min: Number.isFinite(min) ? min : NaN,
    max: Number.isFinite(max) ? max : NaN,
    avail,
    sortBy,
  };
}

function matchesFilters(p, s) {
  if (s.q) {
    const hay = `${p.name} ${p.category} ${p.vendor}`.toLowerCase();
    if (!hay.includes(s.q)) return false;
  }

  const catSet = new Set(s.cats);
  if (catSet.size > 0 && !catSet.has("all") && !catSet.has(p.category)) return false;

  if (s.vendors.length > 0) {
    const pSlug = vendorSlug(p.vendor);
    if (!s.vendors.includes(pSlug)) return false;
  }

  if (Number.isFinite(s.min) && p.price < s.min) return false;
  if (Number.isFinite(s.max) && p.price > s.max) return false;

  if (s.avail === "in" && !p.inStock) return false;
  if (s.avail === "limited" && !p.limited) return false;

  return true;
}

function compareForSort(sortBy) {
  if (sortBy === "price-asc") return (a, b) => a.price - b.price;
  if (sortBy === "price-desc") return (a, b) => b.price - a.price;
  if (sortBy === "rating-desc") return (a, b) => b.rating - a.rating;
  if (sortBy === "name-asc") return (a, b) => a.name.localeCompare(b.name);
  return () => 0;
}

function renderStarsInline(rating) {
  const full = "image/yellow_rating.png";
  const empty = "image/rating.png";
  const filled = Math.max(0, Math.min(5, Math.round(rating)));
  const stars = new Array(5)
    .fill(0)
    .map((_, i) => `<img src="${i < filled ? full : empty}" alt="" aria-hidden="true" />`)
    .join("");
  return `<span class="rating" aria-label="Rating ${rating.toFixed(1)}">${stars} <span class="rating-num">(${rating.toFixed(1)})</span></span>`;
}

function productHref(p) {
  // product.html currently only knows a few ids; keep the link usable anyway.
  return `product.html?id=${encodeURIComponent(p.id)}`;
}

let page = 0;

function render() {
  const s = readState();

  const filtered = PRODUCTS.filter((p) => matchesFilters(p, s));
  const sorted = [...filtered].sort(compareForSort(s.sortBy));

  $("foundCount").textContent = String(sorted.length);

  const maxPage = Math.max(0, Math.ceil(sorted.length / PAGE_SIZE) - 1);
  page = Math.max(0, Math.min(page, maxPage));

  const slice = sorted.slice(page * PAGE_SIZE, page * PAGE_SIZE + PAGE_SIZE);
  const pageBtn = $("pageBtn");
  if (pageBtn) pageBtn.textContent = String(page + 1);

  const grid = $("grid");
  if (slice.length === 0) {
    grid.innerHTML = `<div style="padding:18px;color:rgba(15,23,42,0.65);font-weight:600;">No products match your filters.</div>`;
  } else {
    grid.innerHTML = slice
      .map((p) => {
        const badgeText = p.limited ? "Limited" : "In Stock";
        const badge = `<span class="badge ${p.limited ? "" : "green"}">${badgeText}</span>`;
        const disabled = p.inStock ? "" : "disabled";
        return `
          <article class="card" data-id="${p.id}">
            <div class="card-media">
              <a class="open" href="${productHref(p)}" aria-label="Open ${p.name}">
                <img src="${p.image}" alt="${p.name}" />
              </a>
              ${badge}
            </div>
            <div class="card-body">
              <div class="card-title">
                <a class="open" href="${productHref(p)}" style="text-decoration:none;color:inherit">${p.name}</a>
              </div>
              <p class="vendor">by ${p.vendor}</p>
              <div class="card-rating">
                ${renderStarsInline(p.rating)}
              </div>
              <div class="card-meta">
                <span class="price">${formatMoney(p.price)}</span>
                <span class="unit">${p.unit}</span>
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

  const prev = $("prevBtn");
  const next = $("nextBtn");
  prev.disabled = page <= 0;
  next.disabled = page >= maxPage;
}

function wire() {
  // Normalize vendor checkbox values to match slugs we expect.
  document.querySelectorAll('input[name="vendor"]').forEach((el) => {
    const label = el.parentElement?.textContent?.trim() || "";
    const wanted = el.value || vendorSlug(label);
    el.value = wanted;
  });

  updateCartBadge();
  window.addEventListener("storage", updateCartBadge);

  $("q").addEventListener("input", () => {
    page = 0;
    render();
  });

  $("sortBy").addEventListener("change", () => {
    page = 0;
    render();
  });

  $("applyBtn").addEventListener("click", () => {
    page = 0;
    render();
  });

  document.querySelectorAll('input[name="avail"]').forEach((el) => {
    el.addEventListener("change", () => {
      page = 0;
      render();
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
    if (!btn) return;
    const card = btn.closest("[data-id]");
    if (!card) return;
    const id = card.getAttribute("data-id");
    const p = PRODUCTS.find((x) => x.id === id);
    if (!p || !p.inStock) return;

    upsertCartItem({
      id: p.id,
      name: p.name,
      unitPrice: p.price,
      unitLabel: `/${p.unit}`,
      qty: 1,
      image: p.image,
    });
    updateCartBadge();
  });

  // Keep "All Products" mutually exclusive with other category checks.
  document.querySelectorAll('input[name="cat"]').forEach((el) => {
    el.addEventListener("change", () => {
      const all = document.querySelector('input[name="cat"][value="all"]');
      const others = Array.from(document.querySelectorAll('input[name="cat"]')).filter((x) => x !== all);
      if (el.value === "all" && el.checked) others.forEach((x) => (x.checked = false));
      if (el.value !== "all" && el.checked) all.checked = false;
      if (el.value !== "all" && !others.some((x) => x.checked)) all.checked = true;
    });
  });
}

wire();
render();

