// Script to show users where they fail with inputs
// It's made to be red and go back to normal color again after 1sec

// - Guac

// ___________________________________________________________________________________

document.getElementById('findBtn').addEventListener('click', function() {
    const input = document.getElementById('inputDest');
    const select = document.getElementById('selectOption');

    input.classList.remove('input-error');
    select.classList.remove('input-error');

    let hasError = false;

    if (!input.value.trim()) {
        input.classList.add('input-error');
        hasError = true;
        setTimeout(() => input.classList.remove('input-error'), 1000);
    }

    if (!select.value) {
        select.classList.add('input-error');
        hasError = true;
        setTimeout(() => select.classList.remove('input-error'), 1000);
    }

    if (!hasError) {
        console.log(`Destination: ${input.value}, Valg: ${select.value}`);
    }
});

// ___________________________________________________________________________________

function showNotification(message, type = 'primary') {
    const container = document.getElementById('guac-notification-container');
    const toast = document.createElement('div');
    toast.className = `guac-notification guac-notification-${type} guac-animate`;
    toast.innerText = message;

    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('guac-shake', 'guac-visible');
    }, 50);

    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => container.removeChild(toast), 800);
    }, 3000);
}