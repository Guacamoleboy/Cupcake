/*

    Handles our forgot section with dynamic load of different menus
    - Guac

*/

function showForgotForm(type) {

    const container = document.getElementById("forgot-form-container");
    let formHtml = "";

    switch (type) {

        case "username":
            formHtml = `
                    <form method="post" action="/forgot/username" class="guac-mt-4">
                        <input type="email" name="email" placeholder="Din email" required>
                        <button type="submit" class="guac-btn guac-btn-form">Fortsæt</button>
                </form>
                `;
            break;

        case "resetUsername":
            formHtml = `
                <form method="post" action="/forgot/resetUsername" class="guac-mt-4">
                    <input type="email" name="email" placeholder="Din email" required>
                    <input type="text" name="newUsername" placeholder="Nyt brugernavn" required>
                    <button type="submit" class="guac-btn guac-btn-form">Nulstil Brugernavn</button>
                </form>
            `;
            break;

        case "email":
            formHtml = `
                    <form method="post" action="/forgot/email" class="guac-mt-4">
                        <input type="text" name="username" placeholder="Dit brugernavn" required>
                        <input type="password" name="password" placeholder="Din adgangskode" required>
                        <button type="submit" class="guac-btn guac-btn-form">Vis din Email</button>
                    </form>
                `;
            break;

        case "password":
            formHtml = `
                    <form method="post" action="/forgot/password" class="guac-mt-4">
                        <input type="email" name="email" placeholder="Din email" required>
                        <button type="submit" class="guac-btn guac-btn-form">Nulstil Adgangskode</button>
                    </form>
                `;
            break;
    }

    container.innerHTML = formHtml;
}