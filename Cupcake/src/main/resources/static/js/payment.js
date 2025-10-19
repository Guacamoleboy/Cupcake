/*

    Handles our payment.html site, options, delivery and payment options.
    Written by Guacamoleboy
    Updated 19/10-2025

*/

document.addEventListener("DOMContentLoaded", () => {

    const nextBtn = document.querySelector(".next-btn");

    // TODO - don't think this is needed
    if (!nextBtn) return;

    const deliveryButtons = document.querySelectorAll(".delivery-options button");
    const paymentButtons = document.querySelectorAll(".payment-options button");
    let selectedDelivery = null;
    let selectedPayment = null;

    function updateNextBtnState() {
        if (selectedDelivery && selectedPayment) {
            nextBtn.classList.remove("disabled");
            nextBtn.removeAttribute("aria-disabled");
        } else {
            nextBtn.classList.add("disabled");
            nextBtn.setAttribute("aria-disabled", "true");
        }
    }

    // _____________________________________________________________

    deliveryButtons.forEach(button => {
        button.addEventListener("click", () => {
            deliveryButtons.forEach(b => b.classList.remove("active"));
            button.classList.add("active");
            selectedDelivery = button.dataset.id;
            updateNextBtnState();
        });
    });

    // _____________________________________________________________

    paymentButtons.forEach(button => {
        button.addEventListener("click", () => {
            paymentButtons.forEach(b => b.classList.remove("active"));
            button.classList.add("active");
            selectedPayment = button.dataset.id;
            updateNextBtnState();
        });
    });

    // _____________________________________________________________

    nextBtn.addEventListener("click", async (e) => {
        e.preventDefault();

        if (!selectedDelivery || !selectedPayment) {
            window.location.href = "/payment?error=missingSelection";
            return;
        }

        const deliveryAddress = document.querySelector("#deliveryAddress")?.value || null;

        try {
            const formData = new URLSearchParams();
            formData.append("deliveryMethod", selectedDelivery);
            formData.append("paymentMethod", selectedPayment);
            formData.append("deliveryAddress", deliveryAddress);

            const response = await fetch("/update-payment-info", {
                method: "POST",
                body: formData
            });

            const data = await response.json();
            if (data.success) {
                window.location.href = "/pay";
            } else {
                console.error("Noget gik galt ved opdatering af betalingsinfo");
            }
        } catch (err) {
            console.error("Fejl i fetch:", err);
        }
    });

    updateNextBtnState();

});