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

<!-- Mobile Navbar Start -->
<div class="navbar-responsive">

  <button class="navbar-toggle" aria-label="Åbn menu">&#9776;</button>
  <div class="navbar-logo-responsive">
    <a href="/"><img src="/images/logo/logo-3.svg" alt="Logo Her Broski"></a>
  </div>
  
</div>

<!-- Mobile Overlay Start -->
<div class="navbar-overlay">

  <button class="overlay-close" aria-label="Luk menu">&#10006;</button>
  <ul class="overlay-menu">
    <li><a href="/order">Menu</a></li>
    <li><a href="/custom">Custom Cupcake</a></li>
    <li><a href="/galleri">Galleri</a></li>
    <li><a href="/events">Events</a></li>
    <li><a href="/contact">Kontakt</a></li>
  </ul>
  
  <div class="overlay-login">
    <a href="/login">
      <button class="navbar-login-button">Log Ind</button>
    </a>
  </div>
  
</div>
<!-- Mobile Navbar End -->

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
        const overlayLoginContainer = container.querySelector(".overlay-login");

        if (!profileButtonContainer) return;

        const setLoginButton = (containerElement) => {

            if (!containerElement) return;

            if (!data.loggedIn) {
                containerElement.innerHTML = `
                    <a href="/login">
                        <button class="navbar-login-button">Log Ind</button>
                    </a>
                `;
                return;
            }

            switch (data.role?.toLowerCase()) {
                case "admin":
                    containerElement.innerHTML = `
                        <a href="/admin">
                            <button class="navbar-login-button">Admin Menu</button>
                        </a>
                    `;
                    break;

                default:
                    containerElement.innerHTML = `
                        <a href="/profile">
                            <button class="navbar-login-button">Min Profil</button>
                        </a>
                    `;
                    break;
            }
        };

        setLoginButton(profileButtonContainer);
        setLoginButton(overlayLoginContainer);

    } catch (err) {
        console.error("Could not fetch auth status:", err);
    }
}

loadNavbar().then(() => {

    const toggleButton = document.querySelector(".navbar-toggle");
    const overlay = document.querySelector(".navbar-overlay");
    const closeButton = document.querySelector(".overlay-close");

    if (toggleButton && overlay && closeButton) {

        toggleButton.addEventListener("click", () => {
            overlay.style.display = "flex";
        });

        closeButton.addEventListener("click", () => {
            overlay.style.display = "none";
        });

        overlay.querySelectorAll("a").forEach(link => {
            link.addEventListener("click", () => {
                overlay.style.display = "none";
            });
        });
    }

});