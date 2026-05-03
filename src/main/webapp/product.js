const DEFAULT_PRODUCT_ID = "fresh-tomatoes";

/** @type {Record<string, any>} */
const PRODUCTS = {
  "fresh-tomatoes": {
    id: "fresh-tomatoes",
    category: "Fresh Vegetables",
    name: "Fresh Tomatoes",
    rating: 5.0,
    reviews: 98,
    price: 45,
    oldPrice: 60,
    unit: "kg",
    inStock: true,
    discountText: "25% OFF",
    desc:
      "Farm-fresh tomatoes with rich flavor and firm texture. Perfect for salads, curries, sauces, and everyday cooking. Carefully selected from local farms for freshness and quality.",
    origin: "Green Valley Farm",
    weight: "1 kg",
    type: "Fresh",
    images: [
      "image/fresh_tomato.png",
      "image/fresh_tomato.png",
      "image/fresh_tomato.png",
      "image/fresh_tomato.png",
    ],
  },
  "organic-carrots": {
    id: "organic-carrots",
    category: "Fresh Vegetables",
    name: "Organic Carrots",
    rating: 4.0,
    reviews: 74,
    price: 60,
    oldPrice: 75,
    unit: "kg",
    inStock: true,
    discountText: "20% OFF",
    desc:
      "Crunchy organic carrots grown naturally for better flavor and nutrition. Great for salads, soups, stir-fry, and juicing. Handpicked to keep them fresh and tender.",
    origin: "Organic Harvest",
    weight: "1 kg",
    type: "Organic",
    images: ["image/organic_carrot.png", "image/organic_carrot.png", "image/organic_carrot.png", "image/organic_carrot.png"],
  },
  "fresh-strawberries": {
    id: "fresh-strawberries",
    category: "Fresh Fruits",
    name: "Fresh Strawberries",
    rating: 5.0,
    reviews: 126,
    price: 120,
    oldPrice: 150,
    unit: "kg",
    inStock: true,
    discountText: "20% OFF",
    desc:
      "Sweet and juicy strawberries with vibrant color and fresh aroma. Ideal for desserts, smoothies, fruit bowls, and healthy snacking.",
    origin: "Fresh Fields",
    weight: "1 kg",
    type: "Fresh",
    images: ["image/fresh_strawberries.png", "image/fresh_strawberries.png", "image/fresh_strawberries.png", "image/fresh_strawberries.png"],
  },
  "organic-carrots-2": {
    id: "organic-carrots-2",
    category: "Fresh Vegetables",
    name: "Organic Carrots",
    rating: 5.0,
    reviews: 51,
    price: 60,
    oldPrice: 75,
    unit: "kg",
    inStock: true,
    discountText: "20% OFF",
    desc:
      "A limited stock batch of premium organic carrots from this week’s harvest. Crisp, naturally sweet, and perfect for daily cooking.",
    origin: "Organic Harvest",
    weight: "1 kg",
    type: "Organic",
    images: ["image/organic_carrot.png", "image/organic_carrot.png", "image/organic_carrot.png", "image/organic_carrot.png"],
  },
  "fresh-bell-peppers": {
    id: "fresh-bell-peppers",
    category: "Fresh Vegetables",
    name: "Bell Peppers",
    rating: 5.0,
    reviews: 89,
    price: 80,
    oldPrice: 95,
    unit: "kg",
    inStock: true,
    discountText: "16% OFF",
    desc:
      "Colorful bell peppers with crisp texture and mild sweetness. Great for stir-fry, salads, stuffed recipes, and grilling.",
    origin: "Nature's Best",
    weight: "1 kg",
    type: "Fresh",
    images: ["image/fresh_bell_pepper.png", "image/fresh_bell_pepper.png", "image/fresh_bell_pepper.png", "image/fresh_bell_pepper.png"],
  },
  "fresh-apples": {
    id: "fresh-apples",
    category: "Fresh Fruits",
    name: "Fresh Apples",
    rating: 5.0,
    reviews: 108,
    price: 150,
    oldPrice: 170,
    unit: "kg",
    inStock: true,
    discountText: "12% OFF",
    desc:
      "Crisp, sweet apples packed with natural freshness. Perfect for direct snacking, fruit salads, baking, or fresh juice.",
    origin: "Green Valley Farm",
    weight: "1 kg",
    type: "Fresh",
    images: ["image/fresh_apple.png", "image/fresh_apple.png", "image/fresh_apple.png", "image/fresh_apple.png"],
  },
  "mixed-vegetables": {
    id: "mixed-vegetables",
    category: "Fresh Vegetables",
    name: "Mixed Vegetables",
    rating: 5.0,
    reviews: 65,
    price: 90,
    oldPrice: 110,
    unit: "kg",
    inStock: true,
    discountText: "18% OFF",
    desc:
      "A colorful mix of seasonal vegetables selected for convenience and freshness. Great choice for soups, curries, and stir-fry dishes.",
    origin: "Organic Harvest",
    weight: "1 kg",
    type: "Fresh Mix",
    images: ["image/mix_vegetables.png", "image/mix_vegetables.png", "image/mix_vegetables.png", "image/mix_vegetables.png"],
  },
  "fresh-oranges": {
    id: "fresh-oranges",
    category: "Fresh Fruits",
    name: "Fresh Oranges",
    rating: 5.0,
    reviews: 91,
    price: 100,
    oldPrice: 120,
    unit: "kg",
    inStock: true,
    discountText: "17% OFF",
    desc:
      "Juicy oranges with balanced sweetness and citrus freshness. Ideal for healthy snacks, fresh juice, and vitamin-rich breakfasts.",
    origin: "Fresh Fields",
    weight: "1 kg",
    type: "Fresh",
    images: ["image/fresh_orange.png", "image/fresh_orange.png", "image/fresh_orange.png", "image/fresh_orange.png"],
  },
  "green-cucumbers": {
    id: "green-cucumbers",
    category: "Fresh Vegetables",
    name: "Green Cucumbers",
    rating: 5.0,
    reviews: 82,
    price: 40,
    oldPrice: 55,
    unit: "kg",
    inStock: true,
    discountText: "27% OFF",
    desc:
      "Fresh green cucumbers with crisp bite and cooling taste. Best for salads, raita, sandwiches, and hydration-focused meals.",
    origin: "Nature's Best",
    weight: "1 kg",
    type: "Fresh",
    images: ["image/green_cucumber.png", "image/green_cucumber.png", "image/green_cucumber.png", "image/green_cucumber.png"],
  },
  "fresh-citrus-mix": {
    id: "fresh-citrus-mix",
    category: "Fresh Fruits",
    name: "Fresh Citrus Mix",
    rating: 5.0,
    reviews: 73,
    price: 110,
    oldPrice: 135,
    unit: "kg",
    inStock: true,
    discountText: "19% OFF",
    desc:
      "A handpicked citrus assortment with bright flavor and natural zest. Great for juices, fruit bowls, and refreshing recipes.",
    origin: "Green Valley Farm",
    weight: "1 kg",
    type: "Fresh Mix",
    images: ["image/fresh_citrus_mix.png", "image/fresh_citrus_mix.png", "image/fresh_citrus_mix.png", "image/fresh_citrus_mix.png"],
  },
};

function $(id) {
  return document.getElementById(id);
}

function formatMoney(value) {
  return `Rs.${Number(value).toFixed(0)}`;
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

