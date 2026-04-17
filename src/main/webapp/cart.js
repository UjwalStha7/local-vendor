const TAX_RATE = 0.08;
const DELIVERY_FEE = 5.99;
const FREE_DELIVERY_THRESHOLD = 50;

/** @type {{id:string,name:string,unitPrice:number,unitLabel:string,qty:number,image:string}[]} */
const DEFAULT_CART_ITEMS = [
  {
    id: "tomatoes",
    name: "Fresh Tomatoes",
    unitPrice: 3.99,
    unitLabel: "/ lb",
    qty: 2,
    image: "image/fresh_tomato.png",
  },
  {
    id: "broccoli",
    name: "Organic Broccoli",
    unitPrice: 2.49,
    unitLabel: "/ lb",
    qty: 1,
    image: "image/organic_broccoli.png",
  },
  {
    id: "apples",
    name: "Red Apples",
    unitPrice: 4.99,
    unitLabel: "/ lb",
    qty: 3,
    image: "image/red_apple.png",
  },
  {
    id: "oranges",
    name: "Fresh Oranges",
    unitPrice: 5.49,
    unitLabel: "/ lb",
    qty: 2,
    image: "image/fresh_orange.png",
  },
  {
    id: "carrots",
    name: "Organic Carrots",
    unitPrice: 1.99,
    unitLabel: "/ lb",
    qty: 1,
    image: "image/organic_carrot.png",
  },
  {
    id: "bananas",
    name: "Ripe Bananas",
    unitPrice: 2.99,
    unitLabel: "/ bunch",
    qty: 2,
    image: "image/ripe_banana.png",
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

let cartItems = readCartFromStorage() ?? DEFAULT_CART_ITEMS;

const els = {
  list: document.getElementById("cart-list"),
  count: document.getElementById("cart-count"),
  subtotal: document.getElementById("summary-subtotal"),
  tax: document.getElementById("summary-tax"),
  delivery: document.getElementById("summary-delivery"),
  total: document.getElementById("summary-total"),
  checkout: document.getElementById("checkout-btn"),
};

function formatMoney(value) {
  return `$${value.toFixed(2)}`;
}

function calcSubtotal(items) {
  return items.reduce((sum, item) => sum + item.unitPrice * item.qty, 0);
}

function render() {
  renderList();
  renderSummary();
}

function renderList() {
  els.count.textContent = `${cartItems.length} Items in Cart`;

  if (cartItems.length === 0) {
    els.list.innerHTML = `<div class="item" role="status" aria-live="polite">
      <div class="item-main" style="grid-column: 1 / -1;">
        <p class="item-title" style="margin:0;">Your cart is empty</p>
        <p class="item-sub" style="margin-top:6px;">Add some items to see them here.</p>
      </div>
    </div>`;
    return;
  }

  els.list.innerHTML = cartItems
    .map((item) => {
      const lineTotal = item.unitPrice * item.qty;
      return `
        <article class="item" data-id="${item.id}">
          <div class="item-img">
            <img src="${item.image}" alt="${item.name}" />
          </div>

          <div class="item-main">
            <h3 class="item-title">${item.name}</h3>
            <p class="item-sub">${formatMoney(item.unitPrice)} ${item.unitLabel}</p>

            <div class="item-actions">
              <div class="qty" role="group" aria-label="Quantity for ${item.name}">
                <button type="button" data-action="dec" aria-label="Decrease quantity">−</button>
                <span aria-live="polite">${item.qty}</span>
                <button type="button" data-action="inc" aria-label="Increase quantity">+</button>
              </div>

              <button class="remove-btn" type="button" data-action="remove" aria-label="Remove ${item.name}">
                <img src="image/delete.png" alt="" aria-hidden="true" />
              </button>
            </div>
          </div>

          <div class="item-price">${formatMoney(lineTotal)}</div>
        </article>
      `;
    })
    .join("");
}

function renderSummary() {
  const subtotal = calcSubtotal(cartItems);
  const tax = subtotal * TAX_RATE;
  const delivery = subtotal >= FREE_DELIVERY_THRESHOLD || subtotal === 0 ? 0 : DELIVERY_FEE;
  const total = subtotal + tax + delivery;

  els.subtotal.textContent = formatMoney(subtotal);
  els.tax.textContent = formatMoney(tax);
  els.delivery.textContent = formatMoney(delivery);
  els.total.textContent = formatMoney(total);

  els.checkout.disabled = cartItems.length === 0;
  els.checkout.style.opacity = cartItems.length === 0 ? "0.6" : "1";
  els.checkout.style.cursor = cartItems.length === 0 ? "not-allowed" : "pointer";
}

function updateQty(id, delta) {
  cartItems = cartItems
    .map((item) => (item.id === id ? { ...item, qty: Math.max(1, item.qty + delta) } : item))
    .filter((item) => item.qty > 0);
  writeCartToStorage(cartItems);
  render();
}

function removeItem(id) {
  cartItems = cartItems.filter((item) => item.id !== id);
  writeCartToStorage(cartItems);
  render();
}

els.list.addEventListener("click", (e) => {
  const button = e.target.closest("button");
  if (!button) return;

  const itemEl = button.closest("[data-id]");
  if (!itemEl) return;

  const id = itemEl.getAttribute("data-id");
  const action = button.getAttribute("data-action");

  if (action === "inc") updateQty(id, 1);
  if (action === "dec") updateQty(id, -1);
  if (action === "remove") removeItem(id);
});

els.checkout.addEventListener("click", () => {
  if (cartItems.length === 0) return;
  alert("Checkout flow not implemented yet.");
});

render();

