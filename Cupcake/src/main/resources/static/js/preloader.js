window.addEventListener("load", () => {
    setTimeout(() => {
        const loader = document.querySelector(".preloader");
        if (loader) loader.style.display = "none";
    }, 1000); // !! Must match with the style.css value under .preloader !!
});