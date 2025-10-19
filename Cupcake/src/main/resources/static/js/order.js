document.addEventListener("DOMContentLoaded", () => {

    // Attributes
    const categoryFilter = document.getElementById("category-filter");
    const toppingFilter = document.getElementById("topping-filter");
    const bundFilter = document.getElementById("bund-filter");
    const cupcakeContainer = document.querySelector(".cupcake-cards");
    const resetBtn = document.querySelector(".filter-select.reset-btn");
    const searchInput = document.querySelector(".filter-input");
    const deliveryBtn = document.querySelector(".delivery-options button");
    const paymentBtn = document.querySelector(".payment-options button");
    const paybtn = document.querySelector(".next-btn");

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

    // ___________________________________________________________________

    const deliveryButtons = document.querySelectorAll(".delivery-options button");
    deliveryButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            deliveryButtons.forEach(b => b.classList.remove("selected"));
            btn.classList.add("selected");
        });
    });

    // ___________________________________________________________________

    const paymentButtons = document.querySelectorAll(".payment-options button");
    paymentButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            paymentButtons.forEach(b => b.classList.remove("selected"));
            btn.classList.add("selected");
        });
    });

    paybtn.addEventListener("click", async () => {
        const deliveryMethod = document.querySelector(".delivery-options button.selected")?.textContent || "GLS";
        const paymentMethod = document.querySelector(".payment-options button.selected")?.textContent || "MobilePay";
        const couponCode = document.getElementById("couponCode")?.value || "";

        const formData = new URLSearchParams();
        formData.append("deliveryMethod", deliveryMethod);
        formData.append("paymentMethod", paymentMethod);
        formData.append("couponCode", couponCode);

        try {
            const response = await fetch("/update-payment-info", {
                method: "POST",
                body: formData
            });

            const data = await response.json();
            if (!data.success) {
                console.error("Noget gik galt ved opdatering af betalingsinfo");
            } else {
                console.log("Betaling info gemt!");
            }
        } catch (err) {
            console.error("Fejl i fetch:", err);
        }
    });

});