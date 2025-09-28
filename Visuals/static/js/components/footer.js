// Sets a const to our finished Navbar Visuals 

const footerHTML = `
<!-- Footer Start -->
    <section class="footer">
        <div class="guac-container guac-mb-5 guac-animate guac-slide-up">
            <div class="guac-row guac-justify-between">
                <div class="guac-col-auto footer-col">
                    <h3>Links</h3>
                    <ul>
                        <li><a href="#">Om os</a></li>
                        <li><a href="#">Kundeservice</a></li>
                        <li><a href="#">Kontakt</a></li>
                        <li><a href="#">Bestil Cupcakes</a></li>
                    </ul>
                </div>
                <div class="guac-col-auto footer-col">
                    <h3>Social</h3>
                    <ul>
                        <li><a href="#">Facebook</a></li>
                        <li><a href="#">TikTok</a></li>
                        <li><a href="#">Instagram</a></li>
                        <li><a href="#">YouTube</a></li>
                    </ul>
                </div>
                <div class="guac-col-auto footer-col">
                    <h3>Jobs</h3>
                    <ul>
                        <li><a href="#">Ledige Jobs</a></li>
                        <li><a href="#">Karriere</a></li>
                        <li><a href="#">Ansøg nu</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </section>
    <!-- Footer End -->
`;

export function loadFooter(containerId = "footer-component") {
    
    const container = document.getElementById(containerId);

    if (!container) {
        console.error(`Navbar container #${containerId} not found`);
        return;
    }

    container.innerHTML = footerHTML;

}

loadFooter();