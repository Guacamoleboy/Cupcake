const footerNavHTML = `
    <!-- Footer Navbar Start -->
    <section class="footer-navbar guac-d-flex guac-justify-center guac-align-center">
        <p id="footer-text">
            Built by Andreas, Ebou & Jonas with 
            <span style="color:#ff0000;">&#10084;</span>
        </p>
    </section>
    <!-- Footer Navbar End -->
`;

export async function loadFooterNav(containerId = "footer-nav-component") {

    const container = document.getElementById(containerId);
    if (!container) {
        console.error(`Navbar container #${containerId} not found`);
        return;
    }

    container.innerHTML = footerNavHTML;

    try {
        const response = await fetch("/api/auth/status");
        const data = await response.json();

        if (data.loggedIn && data.email) {
            const footerText = container.querySelector("#footer-text");
            if (footerText) {
                footerText.innerHTML += `
                    <span> | <strong>${data.email}</strong></span>
                `;
            }
        }

    } catch (err) {
        console.error("Could not fetch auth status:", err);
    }
}

loadFooterNav();