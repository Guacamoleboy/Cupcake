/*

    Can most likely be put in another .js file such as payment.js
    Let's see about it later. For now this is the way.

    - Guac

*/

document.addEventListener("DOMContentLoaded", function() {

    // Attributes
    const modal = document.getElementById("paymentModalContainer");
    const openBtn = document.getElementById("openPaymentModalBtn");
    const closeBtn = document.getElementById("paymentModalClose");

    // _______________________________________________________

    openBtn.addEventListener("click", function(e) {
        e.preventDefault();
        modal.style.display = "block";
    });

    // _______________________________________________________

    closeBtn.addEventListener("click", function() {
        modal.style.display = "none";
    });

    // _______________________________________________________

    window.addEventListener("click", function(e) {
        if (e.target === modal) {
            modal.style.display = "none";
        }
    });

});