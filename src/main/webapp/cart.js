/**
 * Shopping cart — localStorage + checkout to database
 */
const CART_KEY = "cartItems";

function readCartFromStorage() {
  try {
    const raw = localStorage.getItem(CART_KEY);
    if (!raw) return [];
    const parsed = JSON.parse(raw);
    if (!Array.isArray(parsed)) return [];
    return parsed
      .filter((x) => x && (x.productId != null || x.id != null))
      .map((x) => ({
        productId: Number(x.productId != null ? x.productId : x.id),
        id: String(x.productId != null ? x.productId : x.id),
        name: String(x.name || ""),
        unitPrice: Number(x.unitPrice || 0),
        unitLabel: String(x.unitLabel || ""),
        qty: Math.max(1, Number(x.qty || 1)),
        image: String(x.image || ""),
      }))
      .filter((x) => x.productId > 0);
  } catch {
    return [];
  }
}

function writeCartToStorage(items) {
  try {
    localStorage.setItem(CART_KEY, JSON.stringify(items));
  } catch {
    // ignore
  }
}

let cartItems = readCartFromStorage();

const els = {
  list: document.getElementById("cart-list"),
  count: document.getElementById("cart-count"),
  subtotal: document.getElementById("summary-subtotal"),
  total: document.getElementById("summary-total"),
  checkout: document.getElementById("checkout-btn"),
};

function getContextPath() {
  if (window.CART_CTX) return window.CART_CTX;
  const p = window.location.pathname;
  const idx = p.indexOf("/cart");
  return idx > 0 ? p.substring(0, idx) : "";
}

function formatMoney(value) {
  return `Rs. ${Number(value).toFixed(2)}`;
}

function calcSubtotal(items) {
  return items.reduce((sum, item) => sum + item.unitPrice * item.qty, 0);
}

function updateCartBadge() {
  const badge = document.getElementById("cart-badge");
  if (!badge) return;
  const totalQty = cartItems.reduce((sum, x) => sum + x.qty, 0);
  badge.textContent = String(totalQty);
}

function render() {
  renderList();
  renderSummary();
  updateCartBadge();
}

function renderList() {
  const totalQty = cartItems.reduce((sum, x) => sum + x.qty, 0);
  els.count.textContent =
    totalQty === 0
      ? "Your cart is empty"
      : `${totalQty} item${totalQty === 1 ? "" : "s"} in cart`;

  if (cartItems.length === 0) {
    els.list.innerHTML = `<div class="item" role="status" aria-live="polite">
      <div class="item-main" style="grid-column: 1 / -1;">
        <p class="item-title" style="margin:0;">Your cart is empty</p>
        <p class="item-sub" style="margin-top:6px;">Browse products and click Add to Cart.</p>
      </div>
    </div>`;
    return;
  }

  const ctx = getContextPath();
  els.list.innerHTML = cartItems
    .map((item) => {
      const lineTotal = item.unitPrice * item.qty;
      const img = item.image.startsWith("http") || item.image.startsWith("/")
        ? item.image.startsWith("/") && !item.image.startsWith(ctx)
          ? ctx + item.image
          : item.image
        : ctx + "/" + item.image.replace(/^\//, "");
      return `
        <article class="item" data-id="${item.productId}">
          <div class="item-img">
            <img src="${img}" alt="${item.name}" />
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
                <img src="${ctx}/image/delete.png" alt="" aria-hidden="true" />
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
  els.subtotal.textContent = formatMoney(subtotal);
  els.total.textContent = formatMoney(subtotal);

  const empty = cartItems.length === 0;
  els.checkout.disabled = empty;
  els.checkout.style.opacity = empty ? "0.6" : "1";
  els.checkout.style.cursor = empty ? "not-allowed" : "pointer";
}

function updateQty(productId, delta) {
  cartItems = cartItems
    .map((item) =>
      item.productId === productId ? { ...item, qty: Math.max(1, item.qty + delta) } : item
    )
    .filter((item) => item.qty > 0);
  writeCartToStorage(cartItems);
  render();
}

function removeItem(productId) {
  cartItems = cartItems.filter((item) => item.productId !== productId);
  writeCartToStorage(cartItems);
  render();
}

if (els.list) {
  els.list.addEventListener("click", (e) => {
    const button = e.target.closest("button");
    if (!button) return;
    const itemEl = button.closest("[data-id]");
    if (!itemEl) return;
    const productId = Number(itemEl.getAttribute("data-id"));
    const action = button.getAttribute("data-action");
    if (action === "inc") updateQty(productId, 1);
    if (action === "dec") updateQty(productId, -1);
    if (action === "remove") removeItem(productId);
  });
}

if (els.checkout) {
  els.checkout.addEventListener("click", async () => {
    if (cartItems.length === 0) return;

    const subtotal = calcSubtotal(cartItems);
    const confirmed = window.confirm(
      `Place order for ${cartItems.length} product line(s)?\n\nTotal: ${formatMoney(subtotal)}\n\nAre you sure?`
    );
    if (!confirmed) return;

    const payload = {
      items: cartItems.map((item) => ({
        productId: item.productId,
        quantity: item.qty,
      })),
    };

    const ctx = getContextPath();
    try {
      const res = await fetch(`${ctx}/checkout`, {
        method: "POST",
        credentials: "same-origin",
        headers: { "Content-Type": "application/json", Accept: "application/json" },
        body: JSON.stringify(payload),
      });
      const data = await res.json().catch(() => ({}));
      if (!res.ok || !data.success) {
        alert(data.message || "Checkout failed. Sign in as a customer and try again.");
        if (res.status === 401 || res.redirected) {
          window.location.href = `${ctx}/login?redirect=${encodeURIComponent("/cart")}`;
        }
        return;
      }
      cartItems = [];
      writeCartToStorage(cartItems);
      render();
      alert(
        `Order placed successfully!\n\nOrder: ${data.orderCode || "ORD-" + data.orderId}\nStatus: Pending`
      );
      window.location.href = `${ctx}/product`;
    } catch {
      alert("Could not reach the server. Try again.");
    }
  });
}

render();
