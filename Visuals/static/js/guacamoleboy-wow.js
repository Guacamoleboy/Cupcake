/*

    Script runs along side with guacamoleboy-wow.css
    Please do NOT change unless you're authorized or
               know what you're doing..

                  Last edited by
                   Guacamoleboy
                    28/09-2025

*/

// __________________________________________________________________

document.addEventListener("DOMContentLoaded", function() {
    // Animationer
    const elements = document.querySelectorAll(".guac-animate");

    function isInViewport(el) {
        const rect = el.getBoundingClientRect();
        return (
            rect.top <= (window.innerHeight || document.documentElement.clientHeight) &&
            rect.bottom >= 0
        );
    }

    function checkAnimations() {
        elements.forEach(el => {
            if (isInViewport(el) && !el.classList.contains("guac-visible")) {
                el.classList.add("guac-visible");
                const animation = el.dataset.guacAnimation;
                if (animation) {
                    el.classList.add(`guac-${animation}`);
                }
            }
        });
    }

    window.addEventListener("scroll", checkAnimations);
    window.addEventListener("resize", checkAnimations);
    checkAnimations();

    // Notifications
    const notifications = [
        { message: 'Daddy Notification', type: 'primary' },
        { message: 'Diddy Notification', type: 'success' },
        { message: 'Epstein Notification', type: 'danger' },
        { message: 'Trump Notification', type: 'warning' },
        { message: 'Hentai Notification', type: 'info' }
    ];

    notifications.forEach(notif => showNotification(notif.message, notif.type));
});

// __________________________________________________________________

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