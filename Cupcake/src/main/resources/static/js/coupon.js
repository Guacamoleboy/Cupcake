document.getElementById("applyCouponBtn").addEventListener("click", async () => {
    const code = document.getElementById("couponCode").value;


    const response = await fetch("/apply-coupon", { method: "POST", body: new URLSearchParams({couponCode: code})});


    const data = await response.json();

    if (data.success) {
        document.getElementById("totalDisplay").textContent = data.discountedTotal.toFixed(2) + " kr";

        document.getElementById("couponCodeText").textContent = "Rabatkode: " + code + " anvendt";
        document.getElementById("couponDiscountText").textContent = "Rabat: " + data.discountPercent + "%";
        document.getElementById("couponInfo").style.display = "block";
    } else {

        console.error("Din coupon kode er findes ikke!", err);
    }
});