/**
 * Customer header user menu — toggle panel on name click, close on outside click / Escape.
 */
(function () {
  var menu = document.querySelector("[data-user-menu]");
  if (!menu) {
    return;
  }

  var trigger = menu.querySelector("[data-user-menu-trigger]");
  var panel = menu.querySelector("[data-user-menu-panel]");
  if (!trigger || !panel) {
    return;
  }

  function isOpen() {
    return trigger.getAttribute("aria-expanded") === "true";
  }

  function setOpen(open) {
    trigger.setAttribute("aria-expanded", open ? "true" : "false");
    panel.hidden = !open;
    menu.classList.toggle("is-open", open);
  }

  function closeMenu() {
    setOpen(false);
  }

  function openMenu() {
    setOpen(true);
  }

  trigger.addEventListener("click", function (event) {
    event.stopPropagation();
    if (isOpen()) {
      closeMenu();
    } else {
      openMenu();
    }
  });

  document.addEventListener("click", function (event) {
    if (!menu.contains(event.target)) {
      closeMenu();
    }
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") {
      closeMenu();
    }
  });
})();
