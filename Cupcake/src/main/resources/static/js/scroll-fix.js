// Fixes Bug #12 where href="#" scrolls on <body>. We don't want that.
// We're gonna scroll by content instead

// - Guac

document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener("click", function (e) {

            const targetId = this.getAttribute("href").substring(1);
            const target = document.getElementById(targetId);

            // Only on our Profile / Admin Menu. They use the same tag.
            const container = this.closest(".profile-actual");

            if (target && container) {
                e.preventDefault();
                container.scrollTo({
                    top: target.offsetTop - 20,
                    behavior: "smooth",
                });
            }

        });
    });
});