/*

    Notification Box Bottom Right
    Written by Guacamoleboy
    Date: 07/10-2025

*/

// Attributes

// _______________________________________________________________________

function showNotification(message, color = "green") {

    const container = document.getElementById('guac-notification-container');
    if (!container) return;

    const notificationBox = document.createElement('div');
    const rootStyles = getComputedStyle(document.documentElement);
    let bgColor;

    switch(color.toLowerCase()) {
        case "green":
            bgColor = rootStyles.getPropertyValue('--cupcake-green');
            break;
        case "orange":
        case "warning":
            bgColor = rootStyles.getPropertyValue('--cupcake-orange');
            break;
        case "red":
        default:
            bgColor = rootStyles.getPropertyValue('--cupcake-red');
            break;
    }

    notificationBox.className = 'guac-notification';
    notificationBox.innerText = message;
    notificationBox.style.backgroundColor = bgColor.trim();

    container.appendChild(notificationBox);

    requestAnimationFrame(() => {
        notificationBox.style.opacity = '1';
        notificationBox.style.transform = 'translateX(0)';
    });

    setTimeout(() => {
        notificationBox.style.opacity = '0';
        notificationBox.style.transform = 'translateX(100%)';
        setTimeout(() => container.removeChild(notificationBox), 500);
    }, 5000);

}

// _______________________________________________________________________


document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get("error");

    const hasWelcomed = sessionStorage.getItem("welcomeMessageShown");

    if (document.getElementById('map') && !hasWelcomed) {
        showNotification("Velkommen tilbage", "green");
        sessionStorage.setItem("welcomeMessageShown", "true");
    }

    switch(error) {
        case "wrongInfo":
            showNotification("Forkert brugernavn eller adgangskode...", "red");
            break;
        case "passwordResetSent":
            showNotification("Kig i din mail", "orange");
            break;
        case "passwordReset":
            showNotification("Adgangskode nulstillet!", "green");
            break;
        case "wrongPassword":
            showNotification("Forkert adgangskode...", "orange");
            break;
        case "accountCreated":
            showNotification("Konto oprettet! Log venligst ind.", "green");
            break;
        case "wrongPassMatch":
            showNotification("Adgangskoderne matcher ikke...", "red");
            break;
        case "accountExists":
            showNotification("Brugernavnet findes allerede...", "orange");
            break;
        case "userNotFound":
            showNotification("Bruger ikke fundet...", "orange");
            break;
        case "missingFields":
            showNotification("Manglende felter...", "red");
            break;
        case "balanceAdded":
            showNotification("Saldo tilføjet!", "green");
            break;
        case "500":
            showNotification("Serverfejl: 500", "red");
            break;
        case "usernameChanged":
            showNotification("Dit brugernavn er ændret!", "green");
            break;
        case "accountDeleted":
            showNotification("Din konto er blevet slettet.", "orange");
            break;
        case "deleteCancelled":
            showNotification("Sletning annulleret.", "orange");
            break;
        case "deleteMissingFields":
            showNotification("Udfyld venligst begge felter.", "red");
            break;
        case "deleteEmailMismatch":
            showNotification("Bekræftelsesmail stemmer ikke overens.", "red");
            break;
        case "deleteNameMismatch":
            showNotification("Brugernavnet matcher ikke den aktuelle bruger.", "red");
            break;
        case "deleteNotLoggedIn":
            showNotification("Du skal være logget ind.", "red");
            break;
        case "deleteError":
            showNotification("Kunne ikke slette konto lige nu.", "red");
            break;
        case "usernameChanged":
            showNotification("Du ændrede dit brugernavn.", "green");
            break;
        case "UsernameError":
            showNotification("Der er opstået en fejl!", "red");
            break;
        case "contactError":
            showNotification("Der er opstået en fejl!", "red");
            break;
        case "newMessage1": // Fixes localStorage notification issues
            const hasSeenCupcakeMessage = localStorage.getItem("seenNewCupcakeMessage");
            if (!hasSeenCupcakeMessage) {
                showNotification("Nye Cupcakes Tilføjet!", "green");
                localStorage.setItem("seenNewCupcakeMessage", "true");
            }
            break;
        case "productNotFound":
            showNotification("Produktet blev ikke fundet.", "red");
            break;
        case "comboNotFound":
            showNotification("Cupcake-kombinationen blev ikke fundet.", "red");
            break;
        case "invalidParams":
            showNotification("Ugyldige parametre tilføjet.", "red");
            break;
        case "dbError":
            showNotification("Database fejl. Prøv igen.", "red");
            break;
        case "cartError":
            showNotification("Der opstod en fejl i kurven.", "red");
            break;
        case "invalidToken":
            showNotification("Koden er ugyldig eller udløbet.", "red");
            break;
        case "emptyCart":
            showNotification("Du har ingen kurv", "red");
            break;
        case "missingSelection":
            showNotification("Vælg venligst levering og betaling", "orange");
            break;
        case "passwordIsReset":
            showNotification("Kig i din mail - Password Reset", "orange");
            break;
        case "emailIsReset":
            showNotification("Kig i din mail - Email Reset", "orange");
            break;
        case "usernameIsReset":
            showNotification("Kig i din mail - Username Reset", "orange");
            break;
        case "noOrderFound":
            showNotification("Kunne ikke finde ordren!", "orange");
            break;

    }

});