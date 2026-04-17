const DEFAULT_PRODUCT_ID = "tomatoes";

/** @type {Record<string, any>} */
const PRODUCTS = {
  tomatoes: {
    id: "tomatoes",
    category: "Fresh Vegetables",
    name: "Organic Cherry Tomatoes",
    rating: 4.8,
    reviews: 127,
    price: 4.99,
    oldPrice: 6.99,
    unit: "500g",
    inStock: true,
    discountText: "29% OFF",
    desc:
      "Premium organic cherry tomatoes, vine-ripened to perfection. These sweet and juicy tomatoes are packed with flavor and nutrients. Grown without synthetic pesticides or fertilizers, our tomatoes are carefully harvested at peak ripeness to ensure maximum taste and nutritional value.",
    origin: "Local Farm, California",
    weight: "500g",
    type: "Organic",
    images: [
      "image/organic_cherry_tomatoes.png",
      "image/organic_cherry_tomatoes.png",
      "image/organic_cherry_tomatoes.png",
      "image/organic_cherry_tomatoes.png",
    ],
  },
  broccoli: {
    id: "broccoli",
    category: "Fresh Vegetables",
    name: "Organic Broccoli",
    rating: 4.6,
    reviews: 83,
    price: 2.49,
    oldPrice: 2.99,
    unit: "1 lb",
    inStock: true,
    discountText: "17% OFF",
    desc:
      "Crisp organic broccoli with vibrant florets and a fresh, clean taste. Perfect for steaming, roasting, or stir-frying.",
    origin: "Local Farm",
    weight: "1 lb",
    type: "Organic",
    images: ["image/organic_broccoli.png", "image/organic_broccoli.png", "image/organic_broccoli.png", "image/organic_broccoli.png"],
  },
  apples: {
    id: "apples",
    category: "Fresh Fruits",
    name: "Red Apples",
    rating: 4.7,
    reviews: 102,
    price: 4.99,
    oldPrice: 5.49,
    unit: "1 lb",
    inStock: true,
    discountText: "9% OFF",
    desc:
      "Sweet, crunchy red apples with a juicy bite. Great for snacks, desserts, and salads.",
    origin: "Orchard Select",
    weight: "1 lb",
    type: "Fresh",
    images: ["image/red_apple.png", "image/red_apple.png", "image/red_apple.png", "image/red_apple.png"],
  },
};

function $(id) {
  return document.getElementById(id);
}

function formatMoney(value) {
  return `$${Number(value).toFixed(2)}`;
}

function getProductIdFromUrl() {
  const url = new URL(window.location.href);
  return url.searchParams.get("id") || DEFAULT_PRODUCT_ID;
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
  localStorage.setItem("cartItems", JSON.stringify(items));
}

function upsertCartItem(item) {
  const cart = readCart();
  const idx = cart.findIndex((x) => x.id === item.id);
  if (idx >= 0) {
    cart[idx] = { ...cart[idx], qty: (cart[idx].qty || 0) + item.qty };
  } else {
    cart.push(item);
  }
  writeCart(cart);
}

function renderStars(value) {
  const starsEl = document.querySelector(".stars");
  if (!starsEl) return;
  const rounded = Math.round(value);
  const html = new Array(5)
    .fill(0)
    .map((_, i) => {
      const filled = i < rounded;
      const src = filled ? "image/yellow_rating.png" : "image/rating.png";
      const alt = filled ? "Filled star" : "Empty star";
      return `<img class="star-img" src="${src}" alt="${alt}" />`;
    })
    .join("");
  starsEl.innerHTML = html;
}

function renderThumbs(images, activeIndex) {
  const thumbs = $("thumbs");
  if (!thumbs) return;
  thumbs.innerHTML = images
    .slice(0, 4)
    .map((src, idx) => {
      const current = idx === activeIndex ? 'aria-current="true"' : "";
      return `<button class="thumb" type="button" data-idx="${idx}" ${current} aria-label="Select image ${idx + 1}">
        <img src="${src}" alt="" />
      </button>`;
    })
    .join("");
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

function main() {
  const id = getProductIdFromUrl();
  const product = PRODUCTS[id] || PRODUCTS[DEFAULT_PRODUCT_ID];

  $("category").textContent = product.category;
  $("title").textContent = product.name;
  $("rating-value").textContent = String(product.rating.toFixed(1));
  $("review-count").textContent = String(product.reviews);
  $("price").textContent = formatMoney(product.price);
  $("old-price").textContent = formatMoney(product.oldPrice);
  $("unit").textContent = `/ ${product.unit}`;
  $("desc").textContent = product.desc;
  $("origin").textContent = product.origin;
  $("weight").textContent = product.weight;
  $("type").textContent = product.type;
  $("discount-badge").textContent = product.discountText || "";

  const pill = $("stock-pill");
  pill.textContent = product.inStock ? "In Stock" : "Out of Stock";
  pill.classList.toggle("out", !product.inStock);

  renderStars(product.rating);

  let activeIndex = 0;
  const hero = $("hero-img");
  hero.src = product.images[0];
  hero.alt = product.name;
  renderThumbs(product.images, activeIndex);

  $("thumbs").addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-idx]");
    if (!btn) return;
    const idx = Number(btn.getAttribute("data-idx"));
    if (!Number.isFinite(idx)) return;
    activeIndex = Math.max(0, Math.min(product.images.length - 1, idx));
    hero.src = product.images[activeIndex];
    renderThumbs(product.images, activeIndex);
  });

  let qty = 1;
  const qtyValue = $("qty-value");
  function setQty(next) {
    qty = Math.max(1, Math.min(99, next));
    qtyValue.textContent = String(qty);
  }

  $("qty-dec").addEventListener("click", () => setQty(qty - 1));
  $("qty-inc").addEventListener("click", () => setQty(qty + 1));

  const wishBtn = $("wish-btn");
  let wished = false;
  wishBtn.addEventListener("click", () => {
    wished = !wished;
    wishBtn.style.borderColor = wished ? "rgba(239, 68, 68, 0.45)" : "rgba(15, 23, 42, 0.08)";
    wishBtn.style.background = wished ? "rgba(254, 226, 226, 0.75)" : "rgba(255, 255, 255, 0.75)";
    showToast(wished ? "Saved to wishlist" : "Removed from wishlist");
  });

  const addBtn = $("add-btn");
  addBtn.disabled = !product.inStock;
  addBtn.style.opacity = product.inStock ? "1" : "0.6";
  addBtn.style.cursor = product.inStock ? "pointer" : "not-allowed";

  addBtn.addEventListener("click", () => {
    if (!product.inStock) return;
    upsertCartItem({
      id: product.id,
      name: product.name,
      unitPrice: product.price,
      unitLabel: `/ ${product.unit}`,
      qty,
      image: product.images[0],
    });
    showToast("Added to cart");
  });
}

main();

