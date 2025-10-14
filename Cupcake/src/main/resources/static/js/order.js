document.addEventListener("DOMContentLoaded", () => {

    // Attributes
    const categoryFilter = document.getElementById("category-filter");
    const toppingFilter = document.getElementById("topping-filter");
    const bundFilter = document.getElementById("bund-filter");
    const cupcakeContainer = document.querySelector(".cupcake-cards");
    const resetBtn = document.querySelector(".filter-select.reset-btn");
    const searchInput = document.querySelector(".filter-input");

    // ___________________________________________________________________

    async function loadCupcakes(url) {
        try {
            const res = await fetch(url);
            if (!res.ok) throw new Error("Fejl ved hentning af cupcakes");

            const html = await res.text();
            const tempDiv = document.createElement("div");
            tempDiv.innerHTML = html;
            const newCards = tempDiv.querySelector(".cupcake-cards");
            if (newCards) {
                cupcakeContainer.innerHTML = newCards.innerHTML;
            }
        } catch (err) {
            console.error(err);
        }
    }

    // ___________________________________________________________________

    categoryFilter?.addEventListener("change", () => {
        const category = categoryFilter.value;
        if (category) loadCupcakes(`/order/category/${category}`);
    });

    // ___________________________________________________________________

    toppingFilter?.addEventListener("change", () => {
        const topping = toppingFilter.value;
        if (topping) loadCupcakes(`/order/topping/${topping}`);
    });

    // ___________________________________________________________________

    bundFilter?.addEventListener("change", () => {
        const bund = bundFilter.value;
        if (bund) loadCupcakes(`/order/bund/${bund}`);
    });

    // ___________________________________________________________________

    searchInput?.addEventListener("input", () => {
        const query = searchInput.value.trim();
        loadCupcakes(`/order/search?q=${encodeURIComponent(query)}`);
    });

    // ___________________________________________________________________

    resetBtn?.addEventListener("click", () => {
        categoryFilter.selectedIndex = 0;
        toppingFilter.selectedIndex = 0;
        bundFilter.selectedIndex = 0;

        loadCupcakes(`/order`);

    });

});