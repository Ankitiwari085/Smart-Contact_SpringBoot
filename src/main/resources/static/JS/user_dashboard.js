document.addEventListener('DOMContentLoaded', function () {
  const body = document.body;
  const toggle = document.getElementById('sidebar-toggle');
  const overlay = document.getElementById('sidebar-overlay');
  const sidebar = document.getElementById('sidebar');
  const content = document.getElementById('content');
  const links = sidebar ? Array.from(sidebar.querySelectorAll('a')) : [];

  if (!toggle || !sidebar || !overlay || !content) {
    // one of the required elements is missing — log and exit gracefully
    console.warn('Sidebar elements missing. Sidebar will not work. Ensure #sidebar, #sidebar-toggle, #sidebar-overlay and #content exist.');
    return;
  }

  const DESKTOP_BREAK = 992;

  function isDesktop() {
    return window.innerWidth >= DESKTOP_BREAK;
  }

  function openSidebar() {
    body.classList.add('sidebar-open');
    toggle.setAttribute('aria-expanded', 'true');
    overlay.setAttribute('aria-hidden', 'false');
  }

  function closeSidebar() {
    body.classList.remove('sidebar-open');
    toggle.setAttribute('aria-expanded', 'false');
    overlay.setAttribute('aria-hidden', 'true');
  }

  function toggleSidebar() {
    if (body.classList.contains('sidebar-open')) closeSidebar();
    else openSidebar();
  }

  // Toggle button clicked
  toggle.addEventListener('click', function (e) {
    e.stopPropagation();
    toggleSidebar();
  });

  // Overlay click closes sidebar (mobile)
  overlay.addEventListener('click', function () {
    closeSidebar();
  });

  // Auto-close when a sidebar link is clicked (mobile only)
  links.forEach(link => {
    link.addEventListener('click', function () {
      if (!isDesktop()) closeSidebar();
    });
  });

  // Close on Escape key
  document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') closeSidebar();
  });

  // Optional: open by default on desktop, closed on mobile.
  function applyInitialState() {
    if (isDesktop()) {
      openSidebar();
    } else {
      closeSidebar();
    }
  }

  applyInitialState();

  // Update on resize (keeps UX consistent when resizing)
  let resizeTimer = null;
  window.addEventListener('resize', function () {
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(function () {
      // on crossing the desktop breakpoint, set open/closed automatically
      if (isDesktop()) openSidebar();
      else closeSidebar();
    }, 120);
  });
});
