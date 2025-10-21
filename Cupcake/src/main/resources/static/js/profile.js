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

    // _________________________________________________________________________

    function showOrder(index) {

        orderCards.forEach(card => card.style.display = 'none');
        orderCards[index].style.display = 'block';

        prevBtn.style.display = index === 0 ? 'none' : 'inline-block';
        nextBtn.style.display = index === orderCards.length - 1 ? 'none' : 'inline-block';

    }

    // _________________________________________________________________________

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

    // _________________________________________________________________________

    nextBtn.addEventListener('click', () => {

        if (currentIndex < orderCards.length - 1) {

            currentIndex++;
            showOrder(currentIndex);

        }

    });

    // _________________________________________________________________________

    prevBtn.addEventListener('click', () => {

        if (currentIndex > 0) {

            currentIndex--;
            showOrder(currentIndex);

        }

    });

    // _________________________________________________________________________

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

            const statusClass = (() => {
                switch (data.status.toLowerCase()) {
                    case "open":
                        return "status-open";
                    case "pending":
                        return "status-pending";
                    case "closed":
                        return "status-finished";
                    default:
                        return "status-default";
                }
            })();

            resultDiv.innerHTML = `
                <div class="order-card">
                    <div class="order-header">
                        <h3>Ordre ID: #${data.id}</h3>
                        <p>Status:
                            <span class="${
                                    data.status.toLowerCase() === 'open' ? 'status-open' :
                                    data.status.toLowerCase() === 'pending' ? 'status-pending' :
                                    data.status.toLowerCase() === 'closed' ? 'status-finished' :
                                    'status-default'
                                    }">
                                ${data.status}
                            </span>
                        </p>
                    </div>
            
                    <div class="order-items">
            
                        <!-- Header row -->
                        <div class="order-list">
                            <div class="order-item guac-row guac-col-auto">
                                <p><strong>Produkt</strong></p>
                                <p><strong>Antal</strong></p>
                                <p><strong>Pris pr stk</strong></p>
                            </div>
                        </div>
            
                        <hr class="section-divider">
            
                        <!-- Produkter -->
                        <div class="order-list">
                            ${data.items.map(item => `
                                <div class="order-item guac-row guac-col-auto">
                                    <p>${item.title}</p>
                                    <p>${item.quantity}</p>
                                    <p>${item.price} kr</p>
                                </div>
                            `).join('')}
                        </div>
            
                        <hr class="section-divider">
            
                        <!-- Total -->
                        <div class="order-list">
                            <div class="order-item guac-row guac-col-auto">
                                <p><strong>Total</strong></p>
                                <p></p>
                                <p>${data.totalPrice} kr</p>
                            </div>
                        </div>
            
                    </div>
            
                    <hr class="section-divider">
            
                    <div>
                        ${data.status.toLowerCase() === 'open' ? `
                            <a href="/orderContinue?orderId=${data.id}" class="guac-btn finish-btn">
                                Fortsæt køb
                            </a>
                            <!--
                            <a href="/removeOrder?orderId=${data.id}" class="guac-btn delete-btn">
                                Slet
                            </a>
                            -->
                        ` : ''}
                    </div>
                </div>
            `;

            hideBtn.style.display = "inline-block";

        } catch (err) {

            window.location.href = "/profile?error=dbError";
            console.error(err);

        }

    });

    // _________________________________________________________________________

    hideBtn.addEventListener("click", () => {

        resultDiv.innerHTML = "";
        hideBtn.style.display = "none";
        searchBtn.style.display = "inline-block";

    });

    // _________________________________________________________________________

    createRefundBtn.addEventListener("click", async () => {

        const orderId = document.getElementById("refundOrderId").value.trim();
        const reason = document.getElementById("refundReason").value.trim();

        if (!orderId || !reason) {
            showNotification("Udfyld både ordre ID og grundlag!", "red");
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

            if (res.ok) {
                showNotification("Returnering oprettet!", "green");
                document.getElementById("refundOrderId").value = "";
                document.getElementById("refundReason").value = "";
            } else {
                const errorMessage = data.error || "Noget gik galt...";
                showNotification(errorMessage, "red");
            }

        } catch (err) {
            console.error(err);
            showNotification("Noget gik galt...", "red");
        }
    });

    // _________________________________________________________________________

    function convertDate(refund) {

        return new Date(refund.createdAt).toLocaleString("da-DK", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit"
        })

    }

    // _________________________________________________________________________

    prevActiveBtn.addEventListener("click", () => {

        if (activeIndex > 0) activeIndex--;
        showActiveRefund(activeIndex);

    });

    // _________________________________________________________________________

    nextActiveBtn.addEventListener("click", () => {

        if (activeIndex < activeRefunds.length - 1) activeIndex++;
        showActiveRefund(activeIndex);

    });

    // _________________________________________________________________________

    prevPastBtn.addEventListener("click", () => {

        if (pastIndex > 0) pastIndex--;
        showPastRefund(pastIndex);

    });

    // _________________________________________________________________________

    nextPastBtn.addEventListener("click", () => {

        if (pastIndex < pastRefunds.length - 1) pastIndex++;
        showPastRefund(pastIndex);

    });

    // _________________________________________________________________________

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

    // _________________________________________________________________________

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

    // _________________________________________________________________________

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

    // _________________________________________________________________________

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

    // _________________________________________________________________________

    function showActiveRefund(index) {
        const data = activeRefunds[index];

        const statusClass = (() => {
            switch (data.status.toLowerCase()) {
                case "open": return "status-open";
                case "pending": return "status-pending";
                case "closed": return "status-finished";
                default: return "status-default";
            }
        })();

        activeContainer.innerHTML = `
            <div class="order-card">
                <p>Returnering ID: #${data.id}</p>
                <p>Ordre ID: #${data.orderId}</p>
                <p>Grund: ${data.reason}</p>
                <p>Status: <span class="${statusClass}">${data.status}</span></p>
                <p>Oprettet: ${convertDate(data)}</p>
            </div>
        `;

        prevActiveBtn.style.display = index === 0 ? "none" : "inline-block";
        nextActiveBtn.style.display = index === activeRefunds.length - 1 ? "none" : "inline-block";
    }

    // _________________________________________________________________________

    function showPastRefund(index) {
        const data = pastRefunds[index];

        const statusClass = (() => {
            switch (data.status.toLowerCase()) {
                case "open": return "status-open";
                case "pending": return "status-pending";
                case "closed": return "status-finished";
                default: return "status-default";
            }
        })();

        pastContainer.innerHTML = `
            <div class="order-card">
                <p>Returnering ID: #${data.id}</p>
                <p>Ordre ID: #${data.orderId}</p>
                <p>Grund: ${data.reason}</p>
                <p>Status: <span class="${statusClass}">${data.status}</span></p>
                <p>Oprettet: ${convertDate(data)}</p>
            </div>
        `;

        prevPastBtn.style.display = index === 0 ? "none" : "inline-block";
        nextPastBtn.style.display = index === pastRefunds.length - 1 ? "none" : "inline-block";
    }

});