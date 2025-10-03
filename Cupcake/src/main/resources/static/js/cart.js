/*
    Shopping Cart Menu
    Written by Guacamoleboy
    Date: 01/10-2025
*/

// _______________________________________________________________________

document.addEventListener("DOMContentLoaded", () => {

    // 5 Dummy Cupcakes
    const cupcakes = [
        { name: "Lorte Cupcake", qty: 1 },
        { name: "Pik Cupcake", qty: 2 },
        { name: "Narko Cupcake", qty: 1 },
        { name: "Epstein Special", qty: 3 },
        { name: "Halloween Cupcake", qty: 1 },
        { name: "Kæmpe lort", qty: 1 },
        { name: "Elsker majs bro..", qty: 1 },
    ];

    // 5000 is a dummy price.. NOT a timer for timeout.
    showCartPopup(cupcakes, 5000);

});

// _______________________________________________________________________

function showCartPopup(items, total) {

    // Initial Status
    let timerTotal = 5000;
    let timer_inital = setTimeout(popupHide, timerTotal)
    let isHovered = false;
    let listItems = "";

    const container = document.getElementById('cart-container');
    const popup = document.createElement('div');

    popup.className = "cart-popup";

    // EventListeners for mouse activities
    popup.addEventListener("mouseenter", () => {
        isHovered = true;
        clearTimeout(timer_inital); // Clears our timer
    });

    // If cart != 0 -> Show a thing

    popup.addEventListener("mouseleave", () => {
        isHovered = false;
        timer_inital = setTimeout(popupHide, timerTotal); // Restarts at 5000ms (5sec).
    });

    // For loop for each item (cupcake in cart)
    for (let i = 0; i < items.length; i++) {
        listItems += `
            <li>
                <span>${items[i].name}</span>
                <input type="number" value="${items[i].qty}" min="1">
            </li>
        `;
    }

    // Html
    popup.innerHTML = `
        <h4>Din Kurv</h4>
        <ul>
            ${listItems}
        </ul>
        <div class="total">
            <span>Total:</span>
            <span>${total}</span>
        </div>
    `;

    // Start
    popup.style.opacity = "0";
    popup.style.transform = "translateX(120%)";

    // Adds out popup to our container
    container.appendChild(popup);

    // Animation
    requestAnimationFrame(() => {
        popup.style.opacity = "1";
        popup.style.transform = "translateX(0)";
    });

    // Hides our popup with animation!
    function popupHide() {

        // Visuals
        popup.style.opacity = "0";
        popup.style.transform = "translateX(120%)";

        // Timeout
        setTimeout(() => {
            container.removeChild(popup);
        }, 400);

    }

}