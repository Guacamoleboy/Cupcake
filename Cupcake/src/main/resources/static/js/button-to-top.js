// Button to top that fades in slowly
// Actives at 500px (31rem) unless modified

// - Guac

// ___________________________________________________________________________________

let scrollToTopBtn = document.querySelector(".scroll-btn");

// ___________________________________________________________________________________

window.onscroll = function() {
    if (document.body.scrollTop > 500 || document.documentElement.scrollTop > 500) {
        scrollToTopBtn.classList.add("show");
    } else {
        scrollToTopBtn.classList.remove("show");
    }
};

// ___________________________________________________________________________________

function scrollToSection() {
    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}