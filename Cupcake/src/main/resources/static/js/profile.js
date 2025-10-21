document.addEventListener('DOMContentLoaded', () => {

    const showOrdersBtn = document.getElementById('showOrdersBtn');
    const ordersContainer = document.getElementById('ordersContainer');
    const orderCards = ordersContainer.querySelectorAll('.order-card');
    const prevBtn = document.getElementById('prevOrderBtn');
    const nextBtn = document.getElementById('nextOrderBtn');
    const nav = document.getElementById('nextAndPrevNav');
    const hideBtn = document.getElementById("hideOrderBtn");
    const searchBtn = document.getElementById("searchOrderBtn");

    let currentIndex = 0;
    let resultDiv;


    function showOrder(index) {

        orderCards.forEach(card => card.style.display = 'none');
        orderCards[index].style.display = 'block';

        prevBtn.style.display = index === 0 ? 'none' : 'inline-block';
        nextBtn.style.display = index === orderCards.length - 1 ? 'none' : 'inline-block';

    }

    showOrdersBtn.addEventListener('click', () => {

        if (ordersContainer.style.display === 'none' || ordersContainer.style.display === '') {

            ordersContainer.style.display = 'block';
            showOrdersBtn.textContent = 'Skjul ordre';
            nav.style.display = 'block';

            if (orderCards.length > 0) {

                currentIndex = 0;
                showOrder(currentIndex);

            }

        } else {

            ordersContainer.style.display = 'none';
            showOrdersBtn.textContent = 'Vis ordre';
            nav.style.display = 'none';

        }
    });

    nextBtn.addEventListener('click', () => {

        if (currentIndex < orderCards.length - 1) {

            currentIndex++;
            showOrder(currentIndex);

        }

    });

    prevBtn.addEventListener('click', () => {

        if (currentIndex > 0) {

            currentIndex--;
            showOrder(currentIndex);

        }

    });

    searchBtn.addEventListener("click", async () => {

        const orderId = document.getElementById("orderIdInput").value.trim();
        resultDiv = document.getElementById("orderResult");

        if (!orderId) {

            console.log("Indtast et ordre id!!")
            return;

        }

        try {

            const form = new FormData();
            form.append("orderId", orderId);
            const res = await fetch("/searchOrder", {
                method: "POST",
                body: form
            });
            const data = await res.json();

            if (!res.ok) {

                window.location.href = "/profile?error=noOrderFound";
                return;

            }

            // Colors for status
            const statusClass = (() => {
                switch (data.status.toLowerCase()) {
                    case "open":
                        return "status-open";
                    case "pending":
                        return "status-pending";
                    case "finished":
                        return "status-finished";
                    default:
                        return "status-default";
                }
            })();

            resultDiv.innerHTML = `
                <div class="order-card">
                    <div class="order-header">
                        <h3>Ordre ID: #${data.id}</h3>
                        <p>Status: <span class="${statusClass}">${data.status}</span></p>
                    </div>

                    <div class="order-items">
                        <p><strong>Produkter:</strong></p>
                        <hr class="section-divider">
                        <ul>
                            ${data.items.map(item => `
                                <li>
                                    <span>${item.title}</span> x
                                    <span>${item.quantity}</span>
                                    <span>${item.price}</span> kr
                                </li>
                            `).join("")}
                        </ul>
                    </div>

                    <hr class="section-divider">

                    <p><strong>Total:</strong> ${data.totalPrice} kr</p>

                    <div class="guac-pt-2">
                        ${data.status.toLowerCase() === "open" ? `
                            <a href="/orderContinue?orderId=${data.id}" class="guac-btn finish-btn">
                                Fortsæt køb
                            </a>
                            <!--
                            <a href="/removeOrder?orderId=${data.id}" class="guac-btn delete-btn">
                                Slet
                            </a>
                            -->
                        ` : ""}
                    </div>
                </div>
            `;

            hideBtn.style.display = "inline-block";

        } catch (err) {

            window.location.href = "/profile?error=dbError";
            console.error(err);

        }

    });

    hideBtn.addEventListener("click", () => {

        resultDiv.innerHTML = "";
        hideBtn.style.display = "none";
        searchBtn.style.display = "inline-block";

    });

});
