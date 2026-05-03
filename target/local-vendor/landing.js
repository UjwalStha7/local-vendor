/**
 * Krishak landing — featured grid + cart badge (localStorage cartItems, same as shop/cart).
 */

const CATALOG = {
  tomatoes: {
    name: "Organic Cherry Tomatoes",
    unitPrice: 4.99,
    unitLabel: "/ 500g",
    image: "image/organic_cherry_tomatoes.png",
  },
  broccoli: {
    name: "Fresh Broccoli",
    unitPrice: 4.99,
    unitLabel: "/ kg",
    image: "image/fresh_broccoli.png",
  },
  apples: {
    name: "Red Apples",
    unitPrice: 4.99,
    unitLabel: "/ 1 lb",
    image: "image/red_apple.png",
  },
  oranges: {
    name: "Fresh Oranges",
    unitPrice: 5.49,
    unitLabel: "/ 1 lb",
    image: "image/fresh_orange.png",
  },
  carrots: {
    name: "Organic Carrots",
    unitPrice: 1.99,
    unitLabel: "/ 1 lb",
    image: "image/organic_carrot.png",
  },
  bananas: {
    name: "Ripe Bananas",
    unitPrice: 2.99,
    unitLabel: "/ 1 bunch",
    image: "image/ripe_banana.png",
  },
  capsicum: {
    name: "Fresh Bell Peppers",
    unitPrice: 4.49,
    unitLabel: "/ kg",
    image: "image/fresh_bell_pepper.png",
  },
  strawberries: {
    name: "Fresh Strawberries",
    unitPrice: 5.49,
    unitLabel: "/ kg",
    image: "image/fresh_strawberries.png",
  },
  lettuce: {
    name: "Organic Lettuce",
    unitPrice: 2.99,
    unitLabel: "/ bunch",
    image: "image/organic_lettuce.png",
  },
  beans: {
    name: "Green Beans",
    unitPrice: 4.49,
    unitLabel: "/ kg",
    image: "image/green_beans.png",
  },
  mixed: {
    name: "Mixed Vegetables",
    unitPrice: 7.49,
    unitLabel: "/ basket",
    image: "image/mix_vegetables.png",
  },
};

/** Featured row matches first reference: names, Rs prices, units, and imagery. */
const FEATURED = [
  {
    id: "strawberries",
    displayName: "Fresh Strawberries",
    rsAmount: "120",
    unitNote: "per kg",
    image: "image/fresh_strawberries.png",
  },
  {
    id: "lettuce",
    displayName: "Organic Lettuce",
    rsAmount: "80",
    unitNote: "per bunch",
    image: "image/organic_lettuce.png",
  },
  {
    id: "capsicum",
    displayName: "Fresh Bell Peppers",
    rsAmount: "100",
    unitNote: "per kg",
    image: "image/fresh_bell_pepper.png",
  },
  {
    id: "beans",
    displayName: "Green Beans",
    rsAmount: "90",
    unitNote: "per kg",
    image: "image/green_beans.png",
  },
  {
    id: "broccoli",
    displayName: "Fresh Broccoli",
    rsAmount: "110",
    unitNote: "per kg",
    image: "image/fresh_broccoli.png",
  },
  {
    id: "mixed",
    displayName: "Mixed Vegetables",
    rsAmount: "150",
    unitNote: "per basket",
    image: "image/mix_vegetables.png",
  },
];

function readCartFromStorage() {
  try {
    const raw = localStorage.getItem("cartItems");
    if (!raw) return null;
    const parsed = JSON.parse(raw);
    if (!Array.isArray(parsed)) return null;
    return parsed
      .filter((x) => x && typeof x.id === "string")
      .map((x) => ({
        id: String(x.id),
        name: String(x.name || ""),
        unitPrice: Number(x.unitPrice || 0),
        unitLabel: String(x.unitLabel || ""),
        qty: Math.max(1, Number(x.qty || 1)),
        image: String(x.image || ""),
      }));
  } catch {
    return null;
  }
}

function writeCartToStorage(items) {
  try {
    localStorage.setItem("cartItems", JSON.stringify(items));
  } catch {
    // ignore
  }
}

function cartQtySum(items) {
  return items.reduce((n, x) => n + x.qty, 0);
}

function updateBadge() {
  const items = readCartFromStorage() ?? [];
  const el = document.getElementById("cart-badge");
  if (!el) return;
  const n = cartQtySum(items);
  el.textContent = String(n);
}

function featuredRow(productId) {
  return FEATURED.find((f) => f.id === productId);
}

function addToCart(productId) {
  const spec = CATALOG[productId];
  if (!spec) return;

  const row = featuredRow(productId);
  const image = row?.image ?? spec.image;
  const name = row?.displayName ?? spec.name;

  const items = readCartFromStorage() ?? [];
  const idx = items.findIndex((x) => x.id === productId);
  if (idx >= 0) {
    items[idx] = {
      ...items[idx],
      qty: items[idx].qty + 1,
      image,
      name,
    };
  } else {
    items.push({
      id: productId,
      name,
      unitPrice: spec.unitPrice,
      unitLabel: spec.unitLabel,
      qty: 1,
      image,
    });
  }
  writeCartToStorage(items);
  updateBadge();
}

let toastTimer = null;
function showToast(message) {
  const el = document.getElementById("toast");
  if (!el) return;
  el.textContent = message;
  el.classList.add("show");
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => el.classList.remove("show"), 2200);
}

function renderFeatured() {
  const grid = document.getElementById("product-grid");
  if (!grid) return;

  grid.innerHTML = FEATURED.map(
    (p) => `
    <article class="product-card" data-product-id="${p.id}">
      <div class="product-card__media">
        <img src="${p.image}" alt="${escapeHtml(p.displayName)}" loading="lazy" width="480" height="480" />
      </div>
      <div class="product-card__body">
        <h3 class="product-card__title">${escapeHtml(p.displayName)}</h3>
        <p class="product-card__price">Rs ${escapeHtml(p.rsAmount)}</p>
        <p class="product-card__unit">${escapeHtml(p.unitNote)}</p>
        <button type="button" class="add-cart" data-add="${p.id}" aria-label="Add ${escapeHtml(p.displayName)} to cart">
          <img src="image/white_cart.png" alt="" width="20" height="20" />
        </button>
      </div>
    </article>
  `
  ).join("");

  grid.addEventListener("click", (e) => {
    const btn = e.target.closest("[data-add]");
    if (!btn) return;
    const id = btn.getAttribute("data-add");
    if (!id || !CATALOG[id]) return;
    addToCart(id);
    const meta = FEATURED.find((x) => x.id === id);
    showToast(meta ? `Added ${meta.displayName} to cart` : "Added to cart");
  });
}

function escapeHtml(s) {
  return String(s)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}

window.addEventListener("storage", (e) => {
  if (e.key === "cartItems") updateBadge();
});

/** If you rename images only in FEATURED, refresh cart lines that came from the landing grid. */
function syncCartImagesFromFeatured() {
  const items = readCartFromStorage();
  if (!items || items.length === 0) return;
  let changed = false;
  const next = items.map((item) => {
    const row = featuredRow(item.id);
    if (!row) return item;
    if (item.image === row.image && item.name === row.displayName) return item;
    changed = true;
    return { ...item, image: row.image, name: row.displayName };
  });
  if (changed) writeCartToStorage(next);
}

renderFeatured();
syncCartImagesFromFeatured();
updateBadge();
