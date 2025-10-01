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
        { message: 'Nye Cupcakes Tilføjet', type: 'success' },
    ];

    notifications.forEach(notif => showNotification(notif.message, notif.type));
});

// _______________________________________________________________________

function showNotification(message, type = 'primary') {
    const container = document.getElementById('guac-notification-container');
    const toast = document.createElement('div');
    toast.className = `guac-notification guac-notification-${type}`;
    toast.innerText = message;

    container.appendChild(toast);

    requestAnimationFrame(() => {
        toast.style.opacity = '1';
        toast.style.transform = 'translateX(0)';
    });

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        setTimeout(() => container.removeChild(toast), 500);
    }, 5000);
}