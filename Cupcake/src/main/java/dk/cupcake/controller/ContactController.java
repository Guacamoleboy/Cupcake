package dk.cupcake.controller;

import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;
import io.javalin.http.Context;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

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

        // Our Info
        String ourEmail = "cupcake@travlr.dk";
        String ourPassword = "Nyepasswordssuttermax123!";

        // Input Info
        String name = ctx.formParam("navn");
        String email = ctx.formParam("email");
        String phoneParam = ctx.formParam("telefon");
        String title = ctx.formParam("emne");
        String msg = ctx.formParam("besked");

        // Validation
        String phone = phoneParam != null && !phoneParam.isEmpty() ? phoneParam : "Ikke angivet";

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
                    """,name, email, phone, title, msg);


        // Email setup
        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.host", "send.one.com");
        p.put("mail.smtp.port", "465");
        p.put("mail.smtp.ssl.enable", "true"); // Important

        // Starting our send session
        Session s = Session.getInstance(p,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(ourEmail, ourPassword);
                    }
                });

        // Try-catch to catch exceptions
        try {

            // Sets our email up
            Message message = new MimeMessage(s);
            message.setFrom(new InternetAddress(ourEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(ourEmail)
            );

            // Adds our subject and msg (text)
            message.setSubject(title);
            message.setText(mailBody);

            // Sends it
            Transport.send(message);

            // Redirects
            ctx.html(ThymeleafSetup.render("tak.html", null));

        } catch (MessagingException e) {

            e.printStackTrace();

        }

    }

} // ContactController end