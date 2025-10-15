document.addEventListener("DOMContentLoaded", async () => {

    const topSelect = document.getElementById("top");
    const bundSelect = document.getElementById("bund");
    const showBtn = document.querySelector(".show-btn");
    const previewImg = document.querySelector(".cupcake-preview img");
    const res = await fetch("/cart/get");
    const data = await res.json();

    cartItems = data.items;
    cartTotal = data.total;

    if (cartItems.length > 0 && typeof showMiniCartButton === "function") {
        showMiniCartButton();
    }

    showBtn.addEventListener("click", async () => {

        const top = topSelect.options[topSelect.selectedIndex]?.text;
        const bund = bundSelect.options[bundSelect.selectedIndex]?.text;

        if (!top || !bund) {
            // TODO skal laves som notification
            alert("Vælg både top og bund!");
            return;
        }

        try {
            const res = await fetch(`/custom/preview?topping=${encodeURIComponent(top)}&bund=${encodeURIComponent(bund)}`);
            if (!res.ok) throw new Error("Ingen cupcake fundet");
            const data = await res.json();
            previewImg.src = data.imageUrl;
        } catch (err) {
            console.error(err);
            // TODO skal laves en placeholder cupcake eller noget fremfor bare at vise cupcake-1.png
            previewImg.src = "/images/products/cupcake-1.png";
        }
    });

});