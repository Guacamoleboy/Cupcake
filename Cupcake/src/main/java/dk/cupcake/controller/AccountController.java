// Packages
package dk.cupcake.controller;

// Imports
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AccountController {

    // Attributes

    // ______________________________________________________

    public static void registerRoutes(Javalin app) {

        AccountController controller = new AccountController();

        app.get("/forgot/username", controller::forgotUsername);
        app.get("/forgot/password", controller::forgotPassword);
        app.get("/forgot/email", controller::forgotEmail);

    }

    // ______________________________________________________

    public void forgotUsername(Context ctx) {
        // TODO her skal man input sin mail, og så får man sit brugernavn sendt pr mail
        ctx.redirect("/login?error=usernameIsReset");
    }

    // ______________________________________________________

    public void forgotPassword(Context ctx) {
        // TODO her skal den sende en mail til den mail man skriver for at kunne reset sin password
        // TODO det vil sige, at man bare skal skrive email, og så får man en godkendelseskode pr mail som er aktiv i 5min.
        ctx.redirect("/login?error=passwordIsReset");
    }

    // ______________________________________________________

    public void forgotEmail(Context ctx) {
        // TODO her skal man kunne se sin mail. Dvs man skriver Password + Username, og så displayer den din mail
        ctx.redirect("/login?error=emailIsReset");
    }

} // AccountController end