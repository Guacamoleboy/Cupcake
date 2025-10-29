/*

    Shopping Cart Menu & Buttons
    Written by Guacamoleboy
    Last Updated: 19/10-2025

*/

// Global Attributes
let cartItems = [];
let cartTotal = 0;
let openCartBtn = null;
let hideTimer = null;
let listItems = "";

// _______________________________________________________________

document.addEventListener("DOMContentLoaded", async () => {

    const cartContainer = document.getElementById("cart-container");
    const cartBtnWrapper = document.querySelector(".cart-btn-wrapper");

    openCartBtn = document.createElement("a");
    openCartBtn.className = "cart-btn-mini";
    openCartBtn.innerHTML = `<i class="fa fa-shopping-cart" aria-hidden="true"></i>`;
    cartBtnWrapper.appendChild(openCartBtn);

    openCartBtn.addEventListener("click", () => {
        const existingPopup = cartContainer.querySelector(".cart-popup");

        if (existingPopup) existingPopup.remove();

        if (cartItems.length > 0) {
            showCartPopup(cartItems, cartTotal, false);
        }
    });

    document.body.addEventListener("click", async (e) => {

        const target = e.target.closest(".add-product");

        if (!target) return;

        e.preventDefault();

        const form = new FormData();
        form.append("id", target.getAttribute("data-id"));
        form.append("name", target.getAttribute("data-name"));
        form.append("price", target.getAttribute("data-price"));
        form.append("description", target.getAttribute("data-description"));
        form.append("topping", target.getAttribute("data-topping"));
        form.append("bottom", target.getAttribute("data-bottom"));

        const res = await fetch("/cart/add", { method: "POST", body: form });
        const data = await res.json();

        cartItems = data.items;
        cartTotal = data.totalPrice;

        showCartPopup(cartItems, cartTotal);

    });

    try {

        const res = await fetch("/cart/get");
        if (!res.ok) return;

        const data = await res.json();
        cartItems = data.items || [];
        cartTotal = data.totalPrice || 0;

        if (cartItems.length > 0) {
            showCartPopup(cartItems, cartTotal, true);
        }

    } catch (err) {

        console.error("Kunne ikke hente kurv:", err);

    }
});

// _______________________________________________________________


function showCartPopup(items, total, isInitial = false) {
    const container = document.getElementById("cart-container");
    const cartBtnWrapper = document.querySelector(".cart-btn-wrapper");

    if (!container || !cartBtnWrapper) return;

    const existing = container.querySelector(".cart-popup");

    if (existing) existing.remove();

    listItems = "";

    for (let i = 0; i < items.length; i++) {
        listItems += `
        <li>
            <span>${items[i].title}</span>
            <div class="cart-controls">
                <button class="minus" onclick="removeFromCart(${i}, 1)">-</button>
                <input type='number' id='qty-${i}' value='${items[i].quantity}' min='1' readonly>
                <button class="plus" onclick="addToCart(${i}, 1)">+</button>
            </div>
        </li>`;
    }

    const popup = document.createElement("div");
    popup.className = "cart-popup";
    popup.innerHTML = `
        <h4>Din kurv</h4>
        <ul>${listItems}</ul>
        <div class="total">
            <span>Total:</span>
            <span>${total.toFixed(2)} kr</span>
        </div>
        <button class="go-payment-btn">Gå til betaling</button>
    `;

    container.appendChild(popup);

    popup.style.opacity = "0";
    popup.style.transform = "translateX(120%)";
    popup.style.transition = "all 0.4s ease";

    requestAnimationFrame(() => {
        popup.style.opacity = "1";
        popup.style.transform = "translateX(0)";
    });

    if (!isInitial) {
        openCartBtn.style.display = "none";
        cartBtnWrapper.style.display = "none";
    }

    const goPaymentBtn = popup.querySelector(".go-payment-btn");

    goPaymentBtn.addEventListener("click", () => {
        window.location.href = "/payment";
    });

    if (hideTimer) clearTimeout(hideTimer);

    hideTimer = setTimeout(() => hidePopup(), 2500);

    popup.addEventListener("mouseenter", () => clearTimeout(hideTimer));
    popup.addEventListener("mouseleave", () => hideTimer = setTimeout(() => hidePopup(), 2500));

    function hidePopup() {
        popup.style.opacity = "0";
        popup.style.transform = "translateX(120%)";
        setTimeout(() => popup.remove(), 400);
        if (items.length > 0) {
            openCartBtn.style.display = "block";
            cartBtnWrapper.style.display = "block";
        }
    }

} // Cart end

// _______________________________________________________________

async function addToCart(index, amount = 1) {

    const input = document.getElementById(`qty-${index}`);
    if (!input) return;

    const item = cartItems[index];
    if (!item) return;

    let newValue = parseInt(input.value) + amount;
    if (isNaN(newValue) || newValue < 1) newValue = 1;
    input.value = newValue; item.quantity = newValue;

    const safeId = item.productId || 0;
    const safeName = item.title || item.name || "Ukendt produkt";
    const safePrice = item.price ?? 0.0;
    const safeDesc = item.description || "";
    const safeTop = item.toppingId || 0;
    const safeBottom = item.bottomId || 0;

    const form = new FormData();
    form.append("id", String(safeId));
    form.append("name", safeName);
    form.append("price", String(safePrice));
    form.append("description", safeDesc);
    form.append("topping", String(safeTop));
    form.append("bottom", String(safeBottom));
    form.append("quantity", String(newValue));

    try {

        const res = await fetch("/cart/add", { method: "POST", body: form });

        if (!res.ok) {

            console.error("Fejl fra server:", res.status, res.statusText);
            return;

        }

        const text = await res.text();
        if (!text || text.trim() === "") {

            console.warn("Server returnerede intet JSON (tom response).");
            return;
        }

        const data = JSON.parse(text);
        cartItems = data.items || cartItems;
        cartTotal = data.totalPrice ?? cartTotal;

        const totalElement = document.querySelector(".cart-popup .total span:last-child");
        if (totalElement) {
            totalElement.textContent = `${cartTotal.toFixed(2)} kr`;
        }

    } catch (err) {

        console.error("Kunne ikke opdatere kurv:", err);

    }

}

// _______________________________________________________________

async function removeFromCart(index, amount = 1) {

    const input = document.getElementById(`qty-${index}`);
    if (!input) return;

    const item = cartItems[index];
    if (!item) return;

    let newValue = parseInt(input.value) - amount;
    if (isNaN(newValue) || newValue < 1) newValue = 0;
    input.value = newValue; item.quantity = newValue;
    const safeId = item.productId || 0;
    const safeTop = item.topping || item.top || 0;
    const safeBottom = item.bottom || item.base || 0;

    const form = new FormData();
    form.append("id", String(safeId));
    form.append("amount", String(amount));
    form.append("topping", String(safeTop));
    form.append("bottom", String(safeBottom));

    try {

        const res = await fetch("/cart/remove", { method: "POST", body: form });

        if (!res.ok) {
            console.error("Fejl fra server:", res.status, res.statusText);
            return;
        }

        const text = await res.text();
        if (!text || text.trim() === "") {

            console.warn("Server returnerede intet JSON (tom response).");
            return;

        }

        const data = JSON.parse(text);
        cartItems = data.items || cartItems;
        cartTotal = data.totalPrice ?? cartTotal;

        const totalElement = document.querySelector(".cart-popup .total span:last-child");
        if (totalElement) {
            totalElement.textContent = `${cartTotal.toFixed(2)} kr`;
        }

        if (newValue <= 0) {

            if (!cartItems || cartItems.length === 0) {
                const popup = document.querySelector(".cart-popup");
                popup.style.opacity = "0";
                popup.style.transform = "translateX(120%)";
                setTimeout(() => popup.remove(), 400);

                openCartBtn.style.display = "none";
                document.querySelector(".cart-btn-wrapper").style.display = "none";
                return;
            }

            showCartPopup(cartItems, cartTotal, true);

        }

    } catch (err) {

        console.error("Kunne ikke opdatere kurv:", err);

    }
}