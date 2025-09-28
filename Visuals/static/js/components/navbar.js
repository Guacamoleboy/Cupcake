// Sets a const to our finished Navbar Visuals 

const navbarHTML = `
<!-- Navbar Start -->
<div class="navbar navbar-sticky guac-animate guac-slide-down">
    <div class="guac-container guac-d-flex guac-justify-between guac-align-center">
        
        <!-- Logo Left Start -->
        <div class="navbar-logo">
            <a href="../templates/index.html">
                <img src="../static/images/logo/logo-3.svg" alt="Logo Her Broski">
            </a>
        </div>
        <!-- Logo Left End -->

        <!-- Menu Items Start -->
        <div class="guac-row guac-justify-center">
            <ul class="navbar-menu guac-d-flex guac-gap-3">
                <li class="dropdown">
                    <a href="#">Cupcakes</a>
                    <ul class="dropdown-menu guac-animate guac-fade-in">
                        <li><a href="#">Chokolade</a></li>
                        <li><a href="#">Vanilje</a></li>
                        <li><a href="#">Red Velvet</a></li>
                        <li><a href="#">Osv...</a></li>
                    </ul>
                </li>
                <li><a href="#">Hitter nu</a></li>
                <li><a href="#">Nossehår</a></li>
                <li><a href="#">Events</a></li>
                <li><a href="#">Kontakt</a></li>
            </ul>
        </div>
        <!-- Menu Items End -->

        <!-- Login Start -->
        <div class="navbar-profile">
            <a href="#">
                <button class="navbar-login-button">Log Ind</button>
            </a>
        </div>
        <!-- Login End -->
        
    </div>
</div>
<!-- Navbar End -->
`;

// Searches for our id="navbar-component" checks if it's not found and reports error in console

export function loadNavbar(containerId = "navbar-component") {
    
    const container = document.getElementById(containerId);

    // Checks if container is found or not. Dev Console Error if not found.
    if (!container) {
        console.error(`Navbar container #${containerId} not found`);
        return;
    }

    // Loads our navbar into DOM
    // Same as this. It's just a variable instead.
    // document.getElementById("navbar-container").innerHTML = navbarHTML;
    container.innerHTML = navbarHTML;

}

// Load Navbar (Calls the function we just created)
loadNavbar();