/*

    Notification Box Bottom Right
    Written by Guacamoleboy
    Date: 01/10-2025

*/

// Attributes

// None right now

// _______________________________________________________________________

document.addEventListener("DOMContentLoaded", function() {

    const notifications = [
        { message: 'Nye Cupcakes Tilføjet' },
    ];

    notifications.forEach(notif => showNotification(notif.message));
});

// _______________________________________________________________________


function showNotification(message) {

    const container = document.getElementById('guac-notification-container');
    const notificationBox = document.createElement('div');
    const rootStyles = getComputedStyle(document.documentElement);
    const bgColor = rootStyles.getPropertyValue('--notification-background');

    notificationBox.className = 'guac-notification';
    notificationBox.innerText = message;
    notificationBox.style.backgroundColor = bgColor.trim();

    // Adds our notificationBox to our container
    container.appendChild(notificationBox);

    requestAnimationFrame(() => {
        notificationBox.style.opacity = '1';
        notificationBox.style.transform = 'translateX(0)';
    });

    // Makes sure our notificationBox is only visible for 5seconds.
    setTimeout(() => {
        notificationBox.style.opacity = '0';
        notificationBox.style.transform = 'translateX(100%)';
        setTimeout(() => container.removeChild(notificationBox), 500);
    }, 5000);

}