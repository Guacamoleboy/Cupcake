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

    const container = document.getElementById('cart-container');
    const popup = document.createElement('div');

    popup.className = "cart-popup";

    let listItems = "";
    for (let i = 0; i < items.length; i++) {
        listItems += `
            <li>
                <span>${items[i].name}</span>
                <input type="number" value="${items[i].qty}" min="1">
            </li>
        `;
    }

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

    container.appendChild(popup);

    // Animation
    requestAnimationFrame(() => {
        popup.style.opacity = "1";
        popup.style.transform = "translateX(0)";
    });

    // Remove our menu after 5sec. Need to implement :hover to disable timer while mouse is in :hover or it's :focused
    setTimeout(() => {

        // Fade out (Opacity)
        popup.style.opacity = "0";
        popup.style.transform = "translateX(120%)";

        setTimeout(() => container.removeChild(popup), 400);
    }, 5000);
}