document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.querySelector(".sidebar-toggle");
  const sidebar = document.querySelector(".sidebar");
  const contentWrapper = document.querySelector(".content-wrapper");



  toggleBtn.addEventListener("click", () => {
    sidebar.classList.toggle("active");
    contentWrapper.classList.toggle("shifted");
  });

  // Auto-close sidebar when link clicked (for mobile UX)
  document.querySelectorAll(".sidebar a").forEach(link => {
    link.addEventListener("click", () => {
      if (window.innerWidth <= 768) {
        sidebar.classList.remove("active");
        contentWrapper.classList.remove("shifted");
      }
    });
  });
});
