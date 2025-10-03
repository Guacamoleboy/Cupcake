document.addEventListener("DOMContentLoaded", () => {

    // Attributes
    const showcase = document.querySelector(".section-1-showcase");
    let startIndex = 0;

    // Hardcoded for now.. Changed 1 and 4 cuz 4 is mad ugly. Like. Frfr. 
    const cupcakes = [
        { img: "images/products/cupcake-1.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-2.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-3.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-4.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-5.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-6.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-7.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-8.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-9.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-10.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-11.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-12.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-13.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-14.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-15.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-16.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-17.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-18.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
        { img: "images/products/cupcake-19.png", name: "Cupcake Title", desc: "Beskrivelse her. Elsker Diddy!" },
    ];

    // _______________________________________________________________________________

    function renderShowcase() {
        const items = showcase.querySelectorAll(".showcase-item");

        for (let i = 0; i < items.length; i++) {
            const cupcake = cupcakes[(startIndex + i) % cupcakes.length];
            const item = items[i];

            item.querySelector("img").src = cupcake.img;
            item.querySelector("h3").textContent = cupcake.name;
            item.querySelector("p").textContent = cupcake.desc;

            // Adds our zoom-in-n-out[i] per picture
            const animationClass = `guac-zoom-in-n-out${i + 1}`;

            item.classList.remove(animationClass);
            void item.offsetWidth; // Makes sure our animation works. Just a backup.
            item.classList.add(animationClass);
        }

        // Sektionens animation (valgfri)
        showcase.classList.add("guac-zoom-in");
        showcase.addEventListener("animationend", function handler() {
            showcase.classList.remove("guac-zoom-in");
            showcase.removeEventListener("animationend", handler);
        });
    }

    // _______________________________________________________________________________

    function nextShowcase() {
        startIndex = (startIndex + 3) % cupcakes.length;
        renderShowcase();
        setTimeout(nextShowcase, 3500);
    }

    // _______________________________________________________________________________


    // Use our methods and functions bby
    renderShowcase();
    setTimeout(nextShowcase, 3500);

});