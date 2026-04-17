const PRODUCTS = [
  {
    id: "tomatoes",
    name: "Organic Cherry Tomatoes",
    price: 4.99,
    unit: "500g",
    image: "image/organic_cherry_tomatoes.png",
  },
  {
    id: "broccoli",
    name: "Organic Broccoli",
    price: 2.49,
    unit: "1 lb",
    image: "image/organic_broccoli.png",
  },
  {
    id: "apples",
    name: "Red Apples",
    price: 4.99,
    unit: "1 lb",
    image: "image/red_apple.png",
  },
  {
    id: "oranges",
    name: "Fresh Oranges",
    price: 5.49,
    unit: "1 lb",
    image: "image/fresh_orange.png",
  },
  {
    id: "carrots",
    name: "Organic Carrots",
    price: 1.99,
    unit: "1 lb",
    image: "image/organic_carrot.png",
  },
  {
    id: "bananas",
    name: "Ripe Bananas",
    price: 2.99,
    unit: "1 bunch",
    image: "image/ripe_banana.png",
  },
];

function formatMoney(value) {
  return `$${Number(value).toFixed(2)}`;
}

function render() {
  const grid = document.getElementById("grid");
  grid.innerHTML = PRODUCTS.map(
    (p) => `
      <article class="card" data-id="${p.id}" tabindex="0" role="link" aria-label="Open ${p.name}">
        <div class="img">
          <img src="${p.image}" alt="${p.name}" />
        </div>
        <div class="body">
          <h3 class="name">${p.name}</h3>
          <div class="meta">
            <span class="price">${formatMoney(p.price)}</span>
            <span class="unit">/ ${p.unit}</span>
          </div>
        </div>
      </article>
    `
  ).join("");

  function openFromEl(el) {
    const id = el.getAttribute("data-id");
    if (!id) return;
    window.location.href = `product.html?id=${encodeURIComponent(id)}`;
  }

  grid.addEventListener("click", (e) => {
    const card = e.target.closest("[data-id]");
    if (!card) return;
    openFromEl(card);
  });

  grid.addEventListener("keydown", (e) => {
    if (e.key !== "Enter" && e.key !== " ") return;
    const card = e.target.closest("[data-id]");
    if (!card) return;
    e.preventDefault();
    openFromEl(card);
  });
}

render();

