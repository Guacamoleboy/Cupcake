/*

    Shopping Cart Menu & Buttons
    Written by Guacamoleboy
    Last Updated: 15/10-2025

*/

// Global Attributes
let cartItems = [];
let cartTotal = 0;
let openCartBtn = null;
let hideTimer = null;

// _______________________________________________________________

document.addEventListener("DOMContentLoaded", async () => {

    const cartContainer = document.getElementById("cart-container");
    const cartBtnWrapper = document.querySelector(".cart-btn-wrapper");

    openCartBtn = document.createElement("a");
    openCartBtn.className = "cart-btn-mini";
    openCartBtn.innerHTML = `<i class="fa fa-shopping-cart" aria-hidden="true"></i>`;
    cartBtnWrapper.appendChild(openCartBtn);

    openCartBtn.addEventListener("click", () => {
        if (cartItems.length > 0) {
            showCartPopup(cartItems, cartTotal);
        }
    });

    try {
        const res = await fetch("/cart/get");
        if (res.ok) {
            const data = await res.json();
            cartItems = data.items;
            cartTotal = data.total;

            if (cartItems.length > 0) {
                showCartPopup(cartItems, cartTotal, true);
            }
        }
    } catch (err) {
        console.error("Kunne ikke hente Diddys kurv", err);
    }

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
        cartTotal = data.total;

        showCartPopup(cartItems, cartTotal);

    });
});

// _______________________________________________________________

function showCartPopup(items, total, isInitial = false) {
    const container = document.getElementById("cart-container");
    const cartBtnWrapper = document.querySelector(".cart-btn-wrapper");

    if (!container || !cartBtnWrapper) return;

    const existing = container.querySelector(".cart-popup");

    if (existing) existing.remove();

    let listItems = "";

    // Readonly for now. Not sure how to make it work with update just yet.
    // TODO remove readonly and fix it so it allows input.

    for (let i = 0; i < items.length; i++) {
        listItems += `<li><span>${items[i].title}</span><input type='number' value='${items[i].quantity}' min='1' readonly></li>`;
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

    // Mindes ikke at denne bliver brugt. Har lukket mit Docker, så fjerner den ikke lige nu.
    // TODO Kig om denne bliver brugt, ellers YEET den.
    if (!isInitial) {
        openCartBtn.style.display = "none";
        cartBtnWrapper.style.display = "none";
    }

    const goPaymentBtn = popup.querySelector(".go-payment-btn");

    // TODO Link content to /payment, så payment har korrekt data ved load
    goPaymentBtn.addEventListener("click", () => {
        window.location.href = "/payment";
    });

    if (hideTimer) clearTimeout(hideTimer);

    hideTimer = setTimeout(() => hidePopup(), 2500);

    // TODO 5sec virker meget længe. Måske 3sec er fint. Skal lige testes lidt senere...
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