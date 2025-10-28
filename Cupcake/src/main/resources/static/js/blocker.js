/*

    Allows us to only display our Website on IPad & Web
    - Guac

*/

document.addEventListener('DOMContentLoaded', () => {

    const minWidth = 768;
    const screenWidth = window.innerWidth;
    const ua = navigator.userAgent;
    const isIpad = /iPad|Macintosh/.test(ua) && 'ontouchend' in document;

    if (screenWidth < minWidth && !isIpad) {
        document.body.innerHTML = `
            <div style="
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                height: 100vh;
                text-align: center;
                background-color: #111;
                color: #fff;
                font-family: sans-serif;
                padding: 2rem;
            ">
                <h1>Olsker Cupcakes!</h1>
                <p>Olsker Cupcakes er ikke helt klar til andet end IPad og Web lige nu..</p>
                <p>Brug venligst en af delene for at komme videre.</p>
            </div>
        `;
        return;
    }

});