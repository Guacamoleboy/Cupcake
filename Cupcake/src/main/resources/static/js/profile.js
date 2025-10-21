document.addEventListener('DOMContentLoaded', () => {

    const showOrdersBtn = document.getElementById('showOrdersBtn');
    const ordersContainer = document.getElementById('ordersContainer');
    const orderCards = ordersContainer.querySelectorAll('.order-card');
    const prevBtn = document.getElementById('prevOrderBtn');
    const nextBtn = document.getElementById('nextOrderBtn');
    const nav = document.getElementById('nextAndPrevNav');
    const hideBtn = document.getElementById("hideOrderBtn");
    const searchBtn = document.getElementById("searchOrderBtn");
    const createRefundBtn = document.getElementById("createRefundBtn");
    const refundResult = document.getElementById("refundResult");
    const activeContainer = document.getElementById("activeReturnsContainer");
    const pastContainer = document.getElementById("pastReturnsContainer");
    const showActiveBtn = document.getElementById("showActiveRefundsBtn");
    const showPastBtn = document.getElementById("showPastRefundsBtn");
    const prevActiveBtn = document.getElementById("prevActiveRefundBtn");
    const nextActiveBtn = document.getElementById("nextActiveRefundBtn");
    const prevPastBtn = document.getElementById("prevPastRefundBtn");
    const nextPastBtn = document.getElementById("nextPastRefundBtn");
    const activeNav = document.getElementById("activeNextAndPrevNav");
    const pastNav = document.getElementById("pastNextAndPrevNav");

    let activeIndex = 0, pastIndex = 0;
    let activeRefunds = [], pastRefunds = [];
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

    createRefundBtn.addEventListener("click", async () => {

        const orderId = document.getElementById("refundOrderId").value.trim();
        const reason = document.getElementById("refundReason").value.trim();

        if (!orderId || !reason) {

            refundResult.textContent = "Udfyld både ordre ID og grundlag!";
            refundResult.style.color = "red";
            return;

        }

        try {

            const form = new FormData();
            form.append("orderId", orderId);
            form.append("reason", reason);

            const res = await fetch("/createRefund", {

                method: "POST",
                body: form

            });

            const data = await res.json();

            if (!res.ok) {

                console.log(`Fejl: ${data.error}`)

            }

        } catch (err) {

            console.log("Noget gik galt ved oprettelse af returnering!")
            console.error(err);

        }

    });

    function convertDate(refund) {

        // Ved ikke hvorfor json ikke kan finde ud af LocalDateTime, men fandt denne metode til at convert TimeStamp
        return new Date(refund.createdAt).toLocaleString("da-DK", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit"
        })

    }

    prevActiveBtn.addEventListener("click", () => {

        if (activeIndex > 0) activeIndex--;
        showActiveRefund(activeIndex);

    });

    nextActiveBtn.addEventListener("click", () => {

        if (activeIndex < activeRefunds.length - 1) activeIndex++;
        showActiveRefund(activeIndex);

    });

    prevPastBtn.addEventListener("click", () => {

        if (pastIndex > 0) pastIndex--;
        showPastRefund(pastIndex);

    });

    nextPastBtn.addEventListener("click", () => {

        if (pastIndex < pastRefunds.length - 1) pastIndex++;
        showPastRefund(pastIndex);

    });

    showActiveBtn.addEventListener("click", async () => {

        if (activeContainer.style.display === "block") {

            activeContainer.style.display = "none";
            activeNav.style.display = "none";
            showActiveBtn.textContent = "Vis returneringer";

        } else {

            await loadActiveRefunds();
            showActiveBtn.textContent = "Skjul returneringer";

        }
    });

    showPastBtn.addEventListener("click", async () => {

        if (pastContainer.style.display === "block") {

            pastContainer.style.display = "none";
            pastNav.style.display = "none";
            showPastBtn.textContent = "Vis returneringer";

        } else {

            await loadPastRefunds();
            showPastBtn.textContent = "Skjul returneringer";

        }
    });

    async function loadActiveRefunds() {

        try {

            const res = await fetch("/getActiveRefunds", { method: "POST" });
            activeRefunds = await res.json();

            if (!res.ok) {

                console.log("Kunne ikke hente aktive returneringer.");
                return;

            }

            if (activeRefunds.length === 0) {

                activeContainer.innerHTML = "<p>Ingen aktive returneringer.</p>";
                activeContainer.style.display = "block";
                activeNav.style.display = "none";
                return;

            }

            activeIndex = 0;
            showActiveRefund(activeIndex);
            activeContainer.style.display = "block";
            activeNav.style.display = "flex";

        } catch (err) {

            console.error(err);
        }
    }

    async function loadPastRefunds() {

        try {

            const res = await fetch("/getClosedRefunds", { method: "POST" });
            pastRefunds = await res.json();
            if (!res.ok) {

                console.log("Kunne ikke hente tidligere returneringer.");
                return;

            }

            if (pastRefunds.length === 0) {

                pastContainer.innerHTML = "<p>Ingen tidligere returneringer.</p>";
                pastContainer.style.display = "block";
                pastNav.style.display = "none";
                return;

            }

            pastIndex = 0;
            showPastRefund(pastIndex);
            pastContainer.style.display = "block";
            pastNav.style.display = "flex";

        } catch (err) {

            console.error(err);
            alert(err.message);

        }
    }

    function showActiveRefund(index) {

        activeContainer.innerHTML = `
            <div class="order-card">
                <p>Returnering ID: #${activeRefunds[index].id}</p>
                <p>Ordre ID: #${activeRefunds[index].orderId}</p>
                <p>Grund: ${activeRefunds[index].reason}</p>
                <p>Status: ${activeRefunds[index].status}</p>
                <p>Oprettet: ${convertDate(activeRefunds[index])}</p>
            </div>
        `;

        prevActiveBtn.style.display = index === 0 ? "none" : "inline-block";
        nextActiveBtn.style.display = index === activeRefunds.length - 1 ? "none" : "inline-block";

    }

    function showPastRefund(index) {

        pastContainer.innerHTML = `
            <div class="order-card">
                <p>Returnering ID: #${activeRefunds[index].id}</p>
                <p>Ordre ID: #${activeRefunds[index].orderId}</p>
                <p>Grund: ${activeRefunds[index].reason}</p>
                <p>Status: ${activeRefunds[index].status}</p>
                <p>Oprettet: ${convertDate(activeRefunds[index])}</p>
            </div>
        `;

        prevPastBtn.style.display = index === 0 ? "none" : "inline-block";
        nextPastBtn.style.display = index === pastRefunds.length - 1 ? "none" : "inline-block";

    }

});
