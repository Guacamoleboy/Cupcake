document.addEventListener("DOMContentLoaded", async () => {
    const nameElement = document.querySelector(".profile-title h3");
    if (!nameElement) return;
    try {
        const response = await fetch("/api/auth/status");
        const data = await response.json();
        if (data.loggedIn && data.username) {
            nameElement.innerHTML = `Hej,<br>${data.username}!`;
        } else {
            nameElement.innerHTML = `Hej,<br>Gæst!`; // Fallback
        }
    } catch (err) {
        console.error("Dev log | Username wasnt found: ", err);
        nameElement.innerHTML = `Hej,<br>Gæst!`; // Fallback
    }
});