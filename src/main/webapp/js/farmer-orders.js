/**
 * Vendor Order Management — order detail modal.
 */
(function () {
  var modal = document.getElementById("orderDetailModal");
  var body = document.getElementById("orderDetailBody");
  var title = document.getElementById("orderDetailTitle");
  var detailUrl = window.VP_ORDER_DETAIL_URL;
  if (!modal || !body || !detailUrl) return;

  function formatMoney(value) {
    return "Rs. " + Number(value).toFixed(2);
  }

  function escapeHtml(text) {
    if (text == null) return "";
    return String(text)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;");
  }

  function openModal() {
    modal.hidden = false;
    modal.setAttribute("aria-hidden", "false");
    document.body.classList.add("vp-modal-open");
  }

  function closeModal() {
    modal.hidden = true;
    modal.setAttribute("aria-hidden", "true");
    document.body.classList.remove("vp-modal-open");
  }

  function renderDetail(data) {
    title.textContent = "Order " + data.orderId;
    var rows = (data.items || [])
      .map(function (item) {
        return (
          "<tr>" +
          "<td>" + escapeHtml(item.productName) + "</td>" +
          "<td>" + escapeHtml(item.unit) + "</td>" +
          "<td>" + item.quantity + "</td>" +
          "<td>" + formatMoney(item.unitPrice) + "</td>" +
          "<td>" + formatMoney(item.lineTotal) + "</td>" +
          "</tr>"
        );
      })
      .join("");

    body.innerHTML =
      '<div class="vp-order-detail">' +
      '<dl class="vp-order-detail__meta">' +
      '<div><dt>Customer</dt><dd>' + escapeHtml(data.customerName) + "</dd></div>" +
      '<div><dt>Phone</dt><dd>' + escapeHtml(data.phone || "—") + "</dd></div>" +
      '<div><dt>Email</dt><dd>' + escapeHtml(data.email || "—") + "</dd></div>" +
      '<div><dt>Date</dt><dd>' + escapeHtml(data.orderDate) + "</dd></div>" +
      '<div><dt>Status</dt><dd><span class="vp-status-pill vp-status-pill--' +
      escapeHtml(data.statusKey) +
      '">' +
      escapeHtml(data.status) +
      "</span></dd></div>" +
      '<div><dt>Line items</dt><dd>' +
      data.lineCount +
      "</dd></div>" +
      '<div><dt>Total quantity</dt><dd>' +
      data.totalQuantity +
      "</dd></div>" +
      '<div><dt>Your total</dt><dd><strong>' +
      formatMoney(data.vendorTotal) +
      "</strong></dd></div>" +
      "</dl>" +
      '<div class="vp-table-wrap">' +
      '<table class="vp-table vp-order-detail__table">' +
      "<thead><tr><th>Product</th><th>Unit</th><th>Qty</th><th>Price</th><th>Line total</th></tr></thead>" +
      "<tbody>" +
      (rows || '<tr><td colspan="5" class="vp-table-empty">No items</td></tr>') +
      "</tbody></table></div></div>";
  }

  function showError(message) {
    title.textContent = "Order details";
    body.innerHTML = '<p class="vp-modal__error" role="alert">' + escapeHtml(message) + "</p>";
  }

  modal.querySelectorAll("[data-modal-close]").forEach(function (el) {
    el.addEventListener("click", closeModal);
  });

  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape" && !modal.hidden) {
      closeModal();
    }
  });

  document.querySelectorAll(".vp-view-btn").forEach(function (btn) {
    btn.addEventListener("click", function () {
      var orderId = btn.getAttribute("data-order-id");
      if (!orderId) return;

      openModal();
      body.innerHTML = '<p class="vp-modal__loading">Loading…</p>';

      fetch(detailUrl + "?orderId=" + encodeURIComponent(orderId), {
        credentials: "same-origin",
        headers: { Accept: "application/json" },
      })
        .then(function (res) {
          return res.json().then(function (data) {
            if (!res.ok) {
              throw new Error(data.error || "Could not load order");
            }
            return data;
          });
        })
        .then(renderDetail)
        .catch(function (err) {
          showError(err.message || "Could not load order details.");
        });
    });
  });
})();
