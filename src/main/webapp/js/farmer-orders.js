/**
 * Vendor Order Management — top-bar search filters table rows client-side.
 */
(function () {
  var searchInput = document.querySelector(".vp-search input[name='q']");
  var table = document.getElementById("ordersTable");
  if (!table) return;

  var rows = table.querySelectorAll("tbody tr[data-order-row]");

  function applySearch() {
    var q = searchInput ? searchInput.value.trim().toLowerCase() : "";
    rows.forEach(function (row) {
      var hay = (row.getAttribute("data-search") || "").toLowerCase();
      row.style.display = !q || hay.indexOf(q) !== -1 ? "" : "none";
    });
  }

  if (searchInput) {
    var initial = new URLSearchParams(window.location.search).get("q");
    if (initial) {
      searchInput.value = initial;
    }
    searchInput.addEventListener("input", applySearch);
    applySearch();
  }

  document.querySelectorAll(".vp-view-btn").forEach(function (btn) {
    btn.addEventListener("click", function () {
      var id = btn.getAttribute("data-order-id");
      if (id) {
        window.alert("Order " + id + " details will open here.");
      }
    });
  });
})();
