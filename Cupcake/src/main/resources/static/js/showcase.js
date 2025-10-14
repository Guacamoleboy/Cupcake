document.addEventListener("DOMContentLoaded", () => {

    // Attributes
    const showcase = document.querySelector(".section-1-showcase");
    let startIndex = 0;

    // Hardcoded for now.. Changed 1 and 4 cuz 4 is mad ugly. Like. Frfr. 
    const cupcakes = [
        { img: "images/products/cupcake-1.png", name: "Det røde bær", desc: "Red velvet bund med frisk jordbærtop." },
        { img: "images/products/cupcake-2.png", name: "Cherry On Top", desc: "Blød vaniljebund toppet med chokoladecreme." },
        { img: "images/products/cupcake-3.png", name: "Hindbærdrøm", desc: "Vaniljebund med sød hindbærtopping." },
        { img: "images/products/cupcake-4.png", name: "Skumtop", desc: "Rig chokoladebund toppet med let flødeskum." },
        { img: "images/products/cupcake-5.png", name: "Sweet Delight", desc: "Vanilje med marshmallow og farverige sprinkles." },
        { img: "images/products/cupcake-6.png", name: "Skykys", desc: "Luftig vanilje med luftig skumtopping." },
        { img: "images/products/cupcake-7.png", name: "Sommerdrøm", desc: "Sandkagebund med frisk skum – en sommerfavorit." },
        { img: "images/products/cupcake-8.png", name: "Julehjerte", desc: "Chokolade og skum i julet harmoni." },
        { img: "images/products/cupcake-9.png", name: "Midnatsskum", desc: "Dyb chokolade med mørk skumtop." },
        { img: "images/products/cupcake-10.png", name: "Double Trouble", desc: "Ren chokolade i to lag – for ægte chokoladeelskere." },
        { img: "images/products/cupcake-11.png", name: "Vanilla Cloud", desc: "Lys vanilje med let flødeskum." },
        { img: "images/products/cupcake-12.png", name: "Velvet Kiss", desc: "Red velvet og skum i blød harmoni." },
        { img: "images/products/cupcake-13.png", name: "Bærlyst", desc: "Chokolade med bær og flødeskum – frisk og intens." },
        { img: "images/products/cupcake-14.png", name: "Kakao Klassik (Jul)", desc: "Ren chokoladekage til juletid." },
        { img: "images/products/cupcake-15.png", name: "Frosty Dream", desc: "Vanilje med iskold topping – perfekt til sommer." },
        { img: "images/products/cupcake-16.png", name: "Fluffy Cocoa", desc: "Chokoladebund med marshmallow fluff." },
        { img: "images/products/cupcake-17.png", name: "Choco Creamy", desc: "Chokolade og vanilje i cremet balance." },
        { img: "images/products/cupcake-18.png", name: "Snefnug", desc: "Julens vaniljekage med let skum." },
        { img: "images/products/cupcake-19.png", name: "Crispy Cocoa", desc: "Chokoladebund toppet med sprød marengs." }
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

            const animationClass = `guac-zoom-in-n-out${i + 1}`;

            item.classList.remove(animationClass);
            void item.offsetWidth;
            item.classList.add(animationClass);
        }

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