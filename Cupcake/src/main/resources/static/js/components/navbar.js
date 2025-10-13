const navbarHTML = `
<!-- Navbar Start -->
<div class="navbar navbar-sticky guac-animate guac-slide-down">
    <div class="guac-container guac-d-flex guac-justify-between guac-align-center">
        
        <!-- Logo Left Start -->
        <div class="navbar-logo">
            <a href="/">
                <img src="/images/logo/logo-3.svg" alt="Logo Her Broski">
            </a>
        </div>
        <!-- Logo Left End -->

        <!-- Menu Items Start -->
        <div class="guac-row guac-justify-center">
            <ul class="navbar-menu guac-d-flex guac-gap-3">
                <li class="dropdown">
                    <a href="/order">Menu</a>
                    <ul class="dropdown-menu guac-animate guac-fade-in">
                        <li><a href="/order">Chokolade</a></li>
                        <li><a href="/order">Vanilje</a></li>
                        <li><a href="/order">Red Velvet</a></li>
                    </ul>
                </li>
                <li><a href="/custom">Custom Cupcake</a></li>
                <li><a href="/galleri">Galleri</a></li>
                <li><a href="/events">Events</a></li>
                <li><a href="/contact">Kontakt</a></li>
            </ul>
        </div>
        <!-- Menu Items End -->

        <!-- Login Start -->
        <div class="navbar-profile">
            <a href="login">
                <button class="navbar-login-button">Log Ind</button>
            </a>
        </div>
        <!-- Login End -->
        
    </div>
</div>
<!-- Navbar End -->
`;

// Searches for our id="navbar-component" checks if it's not found and reports error in console

export async function loadNavbar(containerId = "navbar-component") {

    const container = document.getElementById(containerId);

    if (!container) {
        console.error(`Navbar container #${containerId} not found`);
        return;
    }

    container.innerHTML = navbarHTML;

    try {

        const response = await fetch("/api/auth/status");
        const data = await response.json();
        const profileButtonContainer = container.querySelector(".navbar-profile");

        if (!profileButtonContainer) return;

        if (!data.loggedIn) {
            profileButtonContainer.innerHTML = `
                <a href="/login">
                    <button class="navbar-login-button">Log Ind</button>
                </a>
            `;
            return;
        }

        switch (data.role?.toLowerCase()) {
            case "admin":
                profileButtonContainer.innerHTML = `
                    <a href="/admin">
                        <button class="navbar-login-button">Admin Menu</button>
                    </a>
                `;
                break;

            default:
                profileButtonContainer.innerHTML = `
                    <a href="/profile">
                        <button class="navbar-login-button">Min Profil</button>
                    </a>
                `;
                break;
        }

    } catch (err) {
        console.error("Could not fetch auth status:", err);
    }
}

loadNavbar();