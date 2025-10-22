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

        // hent værdierne fra select felterne
        const topValue = topSelect.value;
        const bundValue = bundSelect.value;
        const topText = topSelect.options[topSelect.selectedIndex]?.text;
        const bundText = bundSelect.options[bundSelect.selectedIndex]?.text;

        if (!topValue || !bundValue) {
            // TODO skal laves som notification
            alert("Vælg både top og bund!");
            return;
        }

        try {
            // brug text værdierne til preview da backend forventer navne
            const res = await fetch(`/custom/preview?topping=${encodeURIComponent(topText)}&bund=${encodeURIComponent(bundText)}`);
            if (!res.ok) throw new Error("Ingen cupcake fundet");
            const data = await res.json();
            previewImg.src = data.imageUrl;
        } catch (err) {
            console.error(err);
            // TODO skal laves en placeholder cupcake eller noget fremfor bare at vise cupcake-1.png
            previewImg.src = "/images/products/cupcake-1.png";
        }
    });

    addBtn.addEventListener("click", async (e) => {
        e.preventDefault();

        // hent både id og text værdier
        const topId = topSelect.value;
        const bundId = bundSelect.value;
        const topText = topSelect.options[topSelect.selectedIndex]?.text;
        const bundText = bundSelect.options[bundSelect.selectedIndex]?.text;

        if (!topId || !bundId) {
            alert("Vælg både top og bund før du tilføjer til kurven!");
            return;
        }

        // custom cupcake med rigtige id'er fra databasen
        const product = {
            id: 1, // custom cupcake product id
            name: "Custom Cupcake",
            price: 49,
            description: `Custom cupcake med ${topText} top og ${bundText} bund`,
            topping: parseInt(topId), // topping id fra database
            bottom: parseInt(bundId)  // bund id fra database
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