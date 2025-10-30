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


        const safeId = 0;
        const safeName = "Custom cupcake";
        const safePrice = 49;
        const safeDesc = `Custom cupcake | ${topText} Top | ${bundText} Bund`;
        const safeTop = topId || 1;
        const safeBottom = bundId || 1;

        const formData = new FormData();
        formData.append("id", String(safeId));
        formData.append("name", safeName);
        formData.append("price", String(safePrice));
        formData.append("description", safeDesc);
        formData.append("topping", String(safeTop));
        formData.append("bottom", String(safeBottom));


        try {
            const res = await fetch("/cart/add", { method: "POST", body: formData });
            if (!res.ok) {
                console.error("Fejl ved tilføjelse til kurv:", res.status, res.statusText);
                return;
            }

            const data = await res.json();

            cartItems = data.items;
            cartTotal = data.totalPrice;
            showCartPopup(cartItems, cartTotal);

            cartItems = cartItems.map(item => {
                if (!item.productId || item.productId === 0) {
                    return {
                        ...item,
                        productId: 0,
                        topping: safeTop,
                        bottom: safeBottom
                    };
                }
                return item;
            });

        } catch (err) {
            console.error("Fejl ved tilføjelse af custom cupcake:", err);
        }
    });

});