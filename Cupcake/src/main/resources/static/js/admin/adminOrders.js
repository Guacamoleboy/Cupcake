document.addEventListener("DOMContentLoaded", () => {

    //#########################
    //### Search user orders###
    //#########################

    const searchBtn = document.getElementById("searchUserOrdersBtn");
    const hideBtn = document.getElementById("hideUserOrdersBtn");
    const container = document.getElementById("userOrdersContainer");
    const nextBtn = document.getElementById("nextUserOrderBtn");
    const prevBtn = document.getElementById("prevUserOrderBtn");
    const nav = document.getElementById("userNextAndPrevNav");

    let orders = [];
    let currentIndex = 0;

    // _________________________________________________________________________

    function renderOrder(index) {

        const order = orders[index];
        if (!order) return;

        const statusClass = (() => {

            switch (order.status.toLowerCase()) {

                case "open": return "status-open";
                case "pending": return "status-pending";
                case "closed": return "status-finished";
                default: return "status-default";

            }

        })();

        container.innerHTML = `
            <div class="order-card">
                <div class="order-header">
                    <h3>Ordre ID: #${order.id}</h3>
                    <p>Status: <span class="${statusClass}">${order.status}</span></p>
                </div>

                <div class="order-items">
                    <div class="order-list">
                        <div class="order-item guac-row guac-col-auto">
                            <p><strong>Produkt</strong></p>
                            <p><strong>Antal</strong></p>
                            <p><strong>Pris pr stk</strong></p>
                        </div>
                    </div>
                    <hr class="section-divider">
                    <div class="order-list">
                        ${order.items.map(item => `
                            <div class="order-item guac-row guac-col-auto">
                                <p>${item.title}</p>
                                <p>${item.quantity}</p>
                                <p>${item.price} kr</p>
                            </div>
                        `).join('')}
                    </div>
                    <hr class="section-divider">
                    <div class="order-list">
                        <div class="order-item guac-row guac-col-auto">
                            <p><strong>Total</strong></p>
                            <p></p>
                            <p>${order.totalPrice} kr</p>
                        </div>
                    </div>
                </div>
            </div>
        `;

        prevBtn.style.display = index === 0 ? "none" : "inline-block";
        nextBtn.style.display = index === orders.length - 1 ? "none" : "inline-block";

    }

    // _________________________________________________________________________

    searchBtn.addEventListener("click", async () => {

        const userId = document.getElementById("searchOrderAdminUserId").value.trim();
        const username = document.getElementById("searchOrderAdminUsername").value.trim();
        const email = document.getElementById("searchOrderAdminEmail").value.trim()

        if (!userId && !username && !email) {

            console.log("Indtast ID, Brugernavn eller Email!")
            return;

        }

        const form = new FormData();
        if (userId) form.append("userId", userId);
        if (username) form.append("username", username);
        if (email) form.append("email", email);

        try {

            const res = await fetch("/admin/searchOrder", {
                method: "POST",
                body: form
            });

            if (!res.ok) {

                console.log("Brugeren findes ikke!")
                return;

            }

            orders = await res.json();
            if (orders.length === 0) {

                container.innerHTML = "<p>Ingen ordrer fundet.</p>";
                container.style.display = "block";
                nav.style.display = "none";
                return;

            }

            currentIndex = 0;
            renderOrder(currentIndex);
            container.style.display = "block";
            nav.style.display = "block";
            hideBtn.style.display = "inline-block";
            searchBtn.style.display = "none";

        } catch (err) {

            console.error(err);

        }

    });

    // _________________________________________________________________________

    hideBtn.addEventListener("click", () => {

        container.innerHTML = "";
        container.style.display = "none";
        nav.style.display = "none";
        hideBtn.style.display = "none";
        searchBtn.style.display = "inline-block";

    });

    // _________________________________________________________________________

    nextBtn.addEventListener("click", () => {

        if (currentIndex < orders.length - 1) {

            currentIndex++;
            renderOrder(currentIndex);

        }

    });

    // _________________________________________________________________________

    prevBtn.addEventListener("click", () => {

        if (currentIndex > 0) {

            currentIndex--;
            renderOrder(currentIndex);

        }

    });




    //##########################
    //### Search orders by ID###
    //##########################

    const OrderByIDhideBtn = document.getElementById("hideOrderBtn");
    const OrderByIDsearchBtn = document.getElementById("searchOrderBtn");
    let resultDiv = document.getElementById("orderResult");

    // _________________________________________________________________________

    OrderByIDsearchBtn.addEventListener("click", async () => {

        const orderId = document.getElementById("orderIdInput").value.trim();


        if (!orderId) {

            console.log("Indtast et ordre id!!")
            return;

        }

        try {

            const form = new FormData();
            form.append("orderId", orderId);
            const res = await fetch("/admin/searchOrderID", {
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
                </div>
            `;

            OrderByIDhideBtn.style.display = "inline-block";

        } catch (err) {

            window.location.href = "/profile?error=dbError";
            console.error(err);

        }

    });

    // _________________________________________________________________________

    OrderByIDhideBtn.addEventListener("click", () => {

        resultDiv.innerHTML = "";
        OrderByIDhideBtn.style.display = "none";
        OrderByIDsearchBtn.style.display = "inline-block";

    });




    //#########################
    //### Manage user orders###
    //#########################

    const manageContainer = document.getElementById("updateDeleteOrderContainer");
    const searchManageBtn = document.getElementById("searchManageOrderBtn");
    const hideManageBtn = document.getElementById("hideManageOrderBtn");
    const manageNav = document.getElementById("manageOrderNextPrev");
    const nextManageBtn = document.getElementById("nextManageOrderBtn");
    const prevManageBtn = document.getElementById("prevManageOrderBtn");

    let manageOrders = [];
    let manageIndex = 0;

    // _________________________________________________________________________

    function renderManageOrder(index) {
        const order = manageOrders[index];
        if (!order) return;

        manageContainer.innerHTML = `
            <div class="order-card">
                <div class="order-header">
                    <h3>Ordre ID: #${order.id}</h3>
                    <p>Status: <span class="status-default">${order.status}</span></p>
                </div>
    
                <div class="order-items">
                    <div class="order-list">
                        <div class="order-item guac-row guac-col-auto">
                            <p><strong>Produkt</strong></p>
                            <p><strong>Antal</strong></p>
                            <p><strong>Pris pr stk</strong></p>
                            <p><strong>Handling</strong></p>
                        </div>
                    </div>
                    <hr class="section-divider">
                    <div class="order-list" id="orderItemsEdit">
                        ${order.items.map((item, i) => `
                            <div class="order-item guac-row guac-col-auto">
                                <p>${item.title}</p>
                                <input type="number" min="1" id="qty_${i}" value="${item.quantity}" class="manage-order-input">
                                <input type="number" min ="0" step="0.01" id="price_${i}" value="${item.price}" class="manage-order-input">
                                <button type="button" class="guac-btn action-btn" onclick="removeItem(${i})">Fjern</button>
                            </div>
                        `).join('')}
                    </div>
                    <hr class="section-divider">
                    <div class="order-list">
                        <div class="order-item guac-row guac-col-auto">
                            <p><strong>Total</strong></p>
                            <p></p>
                            <p>${order.totalPrice} kr</p>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="guac-pt-2">
                <button class="guac-btn action-btn" id="saveManageOrderBtn">Gem ændringer</button>
                <button class="guac-btn action-btn" id="deleteManageOrderBtn">Slet ordre</button>
            </div>
        `;

        prevManageBtn.style.display = index === 0 ? "none" : "inline-block";
        nextManageBtn.style.display = index === manageOrders.length - 1 ? "none" : "inline-block";

        document.getElementById("saveManageOrderBtn").addEventListener("click", async () => {

            const form = new FormData();
            form.append("orderId", order.id);

            order.items.forEach((item, i) => {
                const qty = parseInt(document.getElementById(`qty_${i}`).value);
                const price = parseFloat(document.getElementById(`price_${i}`).value);
                form.append("title", item.title);
                form.append("quantity", isNaN(qty) ? item.quantity : qty);
                form.append("price", isNaN(price) ? item.price : price);
            });

            const res = await fetch("/admin/manageOrder", { method: "POST", body: form });
            if (res.redirected) {
                window.location.href = res.url;
                return;
            }
            if (!res.ok) console.log("Der skete en fejl?");

        });

        document.getElementById("deleteManageOrderBtn").addEventListener("click", async () => {

            //TODO Måske en confirmation på at du vil slette ordre?

            const form = new FormData();
            form.append("orderId", order.id);
            form.append("delete", "true");
            const res = await fetch("/admin/manageOrder", { method: "POST", body: form });
            if (res.redirected) {
                window.location.href = res.url;
                return;
            }
            if (!res.ok) console.log("Der skete en fejl!");
        });
    }

    // _________________________________________________________________________

    searchManageBtn.addEventListener("click", async () => {
        const userId = document.querySelector("#updateDeleteOrderAdmin input[placeholder='41414...']").value.trim();
        const username = document.querySelector("#updateDeleteOrderAdmin input[placeholder='Diddy69...']").value.trim();
        const email = document.querySelector("#updateDeleteOrderAdmin input[placeholder='narko@narko.dk']").value.trim();

        const form = new FormData();
        if (userId) form.append("userId", userId);
        if (username) form.append("username", username);
        if (email) form.append("email", email);

        const res = await fetch("/admin/searchOrder", { method: "POST", body: form });

        if (!res.ok) {

            console.log("Ingen ordre fundet!")
            return;

        }

        manageOrders = await res.json();
        manageIndex = 0;
        renderManageOrder(manageIndex);
        hideManageBtn.style.display = "inline-block";
        manageNav.style.display = "block";
        searchManageBtn.style.display = "none";

    });

    // _________________________________________________________________________

    hideManageBtn.addEventListener("click", () => {

        manageContainer.innerHTML = "";
        hideManageBtn.style.display = "none";
        searchManageBtn.style.display = "inline-block";
        manageNav.style.display = "none";

    });

    // _________________________________________________________________________

    nextManageBtn.addEventListener("click", () => {

        if (manageIndex < manageOrders.length - 1) {

            manageIndex++;
            renderManageOrder(manageIndex);

        }

    });

    // _________________________________________________________________________

    prevManageBtn.addEventListener("click", () => {

        if (manageIndex > 0) {

            manageIndex--;
            renderManageOrder(manageIndex);
        }

    });

    // _________________________________________________________________________

    window.removeItem = function (i) {

        manageOrders[manageIndex].items.splice(i, 1);
        renderManageOrder(manageIndex);

    };




    //####################################################
    //##### Refund/return orders by id & all refunds #####
    //####################################################


    const returnContainer = document.getElementById("returnContainer");
    const searchReturnBtn = document.getElementById("searchReturnBtn");
    const showAllReturnsBtn = document.getElementById("showAllReturnsBtn");
    const hideReturnsBtn = document.getElementById("hideReturnsBtn");
    const returnNav = document.getElementById("returnNav");
    const nextReturnBtn = document.getElementById("nextReturnBtn");
    const prevReturnBtn = document.getElementById("prevReturnBtn");

    let returns = [];
    let returnIndex = 0;

    // _________________________________________________________________________

    function renderReturn(index) {
        const ret = returns[index];
        if (!ret) return;

        returnContainer.innerHTML = `
            <div class="order-card">
                <h3>Retur ID: #${ret.id}</h3>
                <p>Ordre ID: ${ret.orderId}</p>
                <p>Bruger ID: ${ret.userId}</p>
                <p>Status: ${ret.status}</p>
                <p>Begrundelse: ${ret.reason}</p>
            </div>
            <button class="guac-btn action-btn" id="confirmReturnBtn">Bekræft</button>
            <button class="guac-btn action-btn" id="rejectReturnBtn">Afvis</button>
        `;

        prevReturnBtn.style.display = index === 0 ? "none" : "inline-block";
        nextReturnBtn.style.display = index === returns.length - 1 ? "none" : "inline-block";

        document.getElementById("confirmReturnBtn").addEventListener("click", async () => {

            const form = new FormData();
            form.append("returnId", ret.id);
            form.append("action", "confirm");
            await fetch("/admin/returnOrder", { method: "POST", body: form });

        });

        document.getElementById("rejectReturnBtn").addEventListener("click", async () => {

            const form = new FormData();
            form.append("returnId", ret.id);
            form.append("action", "reject");
            await fetch("/admin/returnOrder", { method: "POST", body: form });

        });
    }

    // _________________________________________________________________________

    searchReturnBtn.addEventListener("click", async () => {

        const returnId = document.getElementById("returnIdInput").value.trim();

        const form = new FormData();
        if (returnId) form.append("returnId", returnId);

        const res = await fetch("/admin/returnOrder", { method: "POST", body: form });
        returns = await res.json();
        returnIndex = 0;
        renderReturn(returnIndex);
        hideReturnsBtn.style.display = "inline-block";
        returnNav.style.display = "block";

    });

    // _________________________________________________________________________

    showAllReturnsBtn.addEventListener("click", async () => {

        const res = await fetch("/admin/returnOrderShowAll", { method: "POST" });
        returns = await res.json();
        returnIndex = 0;
        renderReturn(returnIndex);
        showAllReturnsBtn.style.display = "none";
        hideReturnsBtn.style.display = "inline-block";
        returnNav.style.display = "block";

    });

    // _________________________________________________________________________

    hideReturnsBtn.addEventListener("click", () => {

        returnContainer.innerHTML = "";
        hideReturnsBtn.style.display = "none";
        showAllReturnsBtn.style.display = "block";
        returnNav.style.display = "none";

    });

    // _________________________________________________________________________

    nextReturnBtn.addEventListener("click", () => {

        if (returnIndex < returns.length - 1) {

            returnIndex++;
            renderReturn(returnIndex);

        }

    });

    // _________________________________________________________________________

    prevReturnBtn.addEventListener("click", () => {

        if (returnIndex > 0) {

            returnIndex--;
            renderReturn(returnIndex);

        }

    });




    //#########################
    //#### Show all orders ####
    //#########################

    const showAllContainer = document.getElementById("ShowAllOrdersContainer");
    const showAllBtn = document.getElementById("ShowAllOrdersBtn");
    const hideAllOrdersBtn = document.getElementById("hideAllOrdersBtn");
    const showAllNav = document.getElementById("ShowAllNextAndPrevNav");
    const nextShowAllOrderBtn = document.getElementById("nextShowAllOrderBtn");
    const prevShowAllOrderBtn = document.getElementById("prevShowAllOrderBtn");

    let allOrders = [];
    let allOrdersIndex = 0;

    // _________________________________________________________________________

    function renderAllOrders(index) {
        const order = allOrders[index];
        if (!order) return;

        const statusClass = (() => {
            switch (order.status.toLowerCase()) {
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

        showAllContainer.innerHTML = `
            <div class="order-card">
                <div class="order-header">
                    <h3>Ordre ID: #${order.id}</h3>
                    <p>Status: <span class="${statusClass}">${order.status}</span></p>
                </div>
    
                <div class="order-items">
                    <div class="order-list">
                        <div class="order-item guac-row guac-col-auto">
                            <p><strong>Produkt</strong></p>
                            <p><strong>Antal</strong></p>
                            <p><strong>Pris pr stk</strong></p>
                        </div>
                    </div>
                    <hr class="section-divider">
                    <div class="order-list">
                        ${order.items.map(item => `
                            <div class="order-item guac-row guac-col-auto">
                                <p>${item.title}</p>
                                <p>${item.quantity}</p>
                                <p>${item.price} kr</p>
                            </div>
                        `).join('')}
                    </div>
                    <hr class="section-divider">
                    <div class="order-list">
                        <div class="order-item guac-row guac-col-auto">
                            <p><strong>Total</strong></p>
                            <p></p>
                            <p>${order.totalPrice} kr</p>
                        </div>
                    </div>
                </div>
            </div>
        `;

        prevShowAllOrderBtn.style.display = index === 0 ? "none" : "inline-block";
        nextShowAllOrderBtn.style.display = index === allOrders.length - 1 ? "none" : "inline-block";

    }

    // _________________________________________________________________________

    showAllBtn.addEventListener("click", async () => {

        try {

            const res = await fetch("/admin/showOrders", {method: "POST"});

            if (!res.ok) {

                console.log("Kunne ikke hente ordrer!");
                return;

            }

            allOrders = await res.json();

            if (allOrders.length === 0) {

                showAllContainer.innerHTML = "<p>Ingen ordrer fundet.</p>";
                showAllContainer.style.display = "block";
                showAllNav.style.display = "none";
                return;

            }

            allOrdersIndex = 0;
            renderAllOrders(allOrdersIndex);
            showAllContainer.style.display = "block";
            showAllNav.style.display = "block";
            hideAllOrdersBtn.style.display = "inline-block";
            showAllBtn.style.display = "none";

        } catch (err) {

            console.error(err);

        }

    });

    // _________________________________________________________________________

    hideAllOrdersBtn.addEventListener("click", () => {

        showAllContainer.innerHTML = "";
        showAllContainer.style.display = "none";
        showAllNav.style.display = "none";
        hideAllOrdersBtn.style.display = "none";
        showAllBtn.style.display = "inline-block";

    });

    // _________________________________________________________________________

    nextShowAllOrderBtn.addEventListener("click", () => {

        if (allOrdersIndex < allOrders.length - 1) {

            allOrdersIndex++;
            renderAllOrders(allOrdersIndex);

        }

    });

    // _________________________________________________________________________

    prevShowAllOrderBtn.addEventListener("click", () => {

        if (allOrdersIndex > 0) {

            allOrdersIndex--;
            renderAllOrders(allOrdersIndex);

        }

    });

    // _________________________________________________________________________


});
