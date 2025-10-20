package dk.cupcake.controller;

import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;
import io.javalin.http.Context;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class ContactController {

    public static void registerRoutes(Javalin app) {
        ContactController controller = new ContactController();

        app.get("/contact", ctx -> ctx.html(ThymeleafSetup.render("contact.html", null)));
        app.get("/tak", controller::contactUs);
    }

    public void contactUs(Context ctx) {
        String ourEmail = "cupcake@travlr.dk";
        String ourPassword = "Nyepasswordssuttermax123!";
        String sendToThisEmail = "andreas.sggamin@gmail.com";

        String name = ctx.queryParam("navn");
        String email = ctx.queryParam("email");
        String phoneParam = ctx.queryParam("telefon");
        String phone = phoneParam != null && !phoneParam.isEmpty() ? phoneParam : "Ikke angivet";
        String title = ctx.queryParam("emne");
        String msg = ctx.queryParam("besked");

        String mailBody = String.format("""
                    Name:
                    %s

                    E-mail:
                    %s

                    Phone:
                    %s

                    Title:
                    %s

                    Description:
                    %s
                    """,name, email,
                phone,
                title, msg);


        // Email setup
        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", "send.one.com");
        p.put("mail.smtp.port", "465");

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
                    Message.RecipientType.TO, InternetAddress.parse(sendToThisEmail)
            );

            // Adds our subject and msg (text)
            message.setSubject(title);
            message.setText(mailBody);

            // Sends it
            Transport.send(message);

            ctx.html(ThymeleafSetup.render("tak.html", null));

        } catch (MessagingException e) {

            System.out.println("Mailen kunne ikke findes!!!");
            e.printStackTrace();

        }


    }

}
