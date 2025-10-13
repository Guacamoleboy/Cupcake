// Sets a const to our finished Navbar Visuals 

const footerHTML = `
<!-- Footer Start -->
    <section class="footer">
        <div class="guac-container guac-mb-5 guac-animate guac-slide-up">
            <div class="guac-row guac-justify-between">
                <div class="guac-col-auto footer-col">
                    <h3>Links</h3>
                    <ul>
                        <li><a href="/">Om os</a></li>
                        <li><a href="/contact">Kundeservice</a></li>
                        <li><a href="/order">Bestil Cupcakes</a></li>
                    </ul>
                </div>
                <div class="guac-col-auto footer-col">
                    <h3>Social</h3>
                    <ul>
                        <li><a href="https://www.facebook.com" target="_blank">Facebook</a></li>
                        <li><a href="https://www.tiktok.com" target="_blank">TikTok</a></li>
                        <li><a href="https://www.instagram.com" target="_blank">Instagram</a></li>
                        <li><a href="https://www.youtube.com" target="_blank">YouTube</a></li>
                    </ul>
                </div>
                <div class="guac-col-auto footer-col">
                    <h3>Jobs</h3>
                    <ul>
                        <li><a href="/jobs">Ledige Jobs</a></li>
                        <li><a href="/career">Karriere</a></li>
                        <li><a href="/apply">Ansøg nu</a></li>
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