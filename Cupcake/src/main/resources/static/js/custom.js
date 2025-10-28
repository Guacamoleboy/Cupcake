document.addEventListener("DOMContentLoaded", async () => {

    const topSelect = document.getElementById("top");
    const bundSelect = document.getElementById("bund");
    const showBtn = document.querySelector(".show-btn");
    const addBtn = document.querySelector(".add-btn");
    const previewImg = document.querySelector(".cupcake-preview img");
    const res = await fetch("/cart/get");
    const data = await res.json();

    cartItems = data.items;
    cartTotal = data.total;

    if (cartItems.length > 0 && typeof showMiniCartButton === "function") {
        showMiniCartButton();
    }

    showBtn.addEventListener("click", async () => {

        const topValue = topSelect.value;
        const bundValue = bundSelect.value;
        const topText = topSelect.options[topSelect.selectedIndex]?.text;
        const bundText = bundSelect.options[bundSelect.selectedIndex]?.text;

        if (!topValue || !bundValue) {
            showNotification("Vælg både Top & Bund", "orange");
            return;
        }

        try {
            const res = await fetch(`/custom/preview?topping=${encodeURIComponent(topText)}&bund=${encodeURIComponent(bundText)}`);
            if (!res.ok) throw new Error("Ingen cupcake fundet");
            const data = await res.json();
            previewImg.src = data.imageUrl;
        } catch (err) {
            console.error(err);
            previewImg.src = "/images/products/cupcake-1.png";
        }
    });

    addBtn.addEventListener("click", async (e) => {
        e.preventDefault();

        const topId = topSelect.value;
        const bundId = bundSelect.value;
        const topText = topSelect.options[topSelect.selectedIndex]?.text;
        const bundText = bundSelect.options[bundSelect.selectedIndex]?.text;

        if (!topId || !bundId) {
            showNotification("Vælg både Top & Bund", "orange");
            return;
        }

        const product = {
            id: 1,
            name: "Custom Cupcake",
            price: 49,
            description: `Custom cupcake | ${topText} Top | ${bundText} Bund`,
            topping: parseInt(topId),
            bottom: parseInt(bundId)
        };

        const formData = new FormData();
        for (const key in product) {
            formData.append(key, product[key]);
        }

        try {
            const res = await fetch("/cart/add", { method: "POST", body: formData });
            if (!res.ok) {
                console.error("Fejl ved tilføjelse til kurv:", res.status, res.statusText);
                return;
            }

            const data = await res.json();

            cartItems = data.items;
            cartTotal = data.total;
            showCartPopup(cartItems, cartTotal);

        } catch (err) {
            console.error("Fejl ved tilføjelse af custom cupcake:", err);
        }
    });

});