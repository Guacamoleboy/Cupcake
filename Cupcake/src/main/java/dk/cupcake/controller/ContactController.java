// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;
import io.javalin.http.Context;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import dk.cupcake.server.MailSetup;

public class ContactController {

    // Attributes

    // ___________________________________________________________

    public static void registerRoutes(Javalin app) {
        ContactController controller = new ContactController();

        app.get("/contact", ctx -> ctx.html(ThymeleafSetup.render("contact.html", null)));
        app.post("/tak", controller::contactUs);

    }

    // ___________________________________________________________

    public void contactUs(Context ctx) {

        String name = ctx.formParam("navn");
        String email = ctx.formParam("email");
        String phoneParam = ctx.formParam("telefon");
        String title = ctx.formParam("emne");
        String msg = ctx.formParam("besked");

        // Validation
        String phone = (phoneParam != null && !phoneParam.isEmpty()) ? phoneParam : "Ikke angivet";

        String mailBody = String.format("""
                Ny besked modtaget

                Navn:
                %s

                E-mail:
                %s

                Telefonnummer:
                %s

                Emne:
                %s

                Besked:
                %s
                """, name, email, phone, title, msg);

        boolean sent = MailSetup.sendMail("cupcake@travlr.dk", title, mailBody);

        if (sent) {
            ctx.html(ThymeleafSetup.render("tak.html", null));
        } else {
            ctx.redirect("/contact?error=contactError");
        }

    }

} // ContactController end