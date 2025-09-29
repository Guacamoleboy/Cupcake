// Sets a const to our finished Navbar Visuals 

const footerNavHTML = `
    <!-- Footer Navbar Start -->
    <section class="footer-navbar guac-d-flex guac-justify-center guac-align-center">
        <p>
            Built by Andreas, Ebou & Jonas with 
            <span style="color:#ff0000;">&#10084;</span>
        </p>
    </section>
    <!-- Footer Navbar End -->
`;

export function loadFooterNav(containerId = "footer-nav-component") {
    
    const container = document.getElementById(containerId);

    if (!container) {
        console.error(`Navbar container #${containerId} not found`);
        return;
    }

    container.innerHTML = footerNavHTML;

}

loadFooterNav();