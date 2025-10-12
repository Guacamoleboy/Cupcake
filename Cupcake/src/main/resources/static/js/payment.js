/*

    Sets :active on button press. Made in their own sections
    to implement backend later.

    - Guac

*/

/* Delivery */
document.querySelectorAll('.delivery-options button').forEach(button => {
    button.addEventListener('click', () => {
        document.querySelectorAll('.delivery-options button').forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');
    });
});

/* Payment */
document.querySelectorAll('.payment-options button').forEach(button => {
    button.addEventListener('click', () => {
        document.querySelectorAll('.payment-options button').forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');
    });
});