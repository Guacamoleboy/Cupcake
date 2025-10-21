// Package
package dk.cupcake.server;

// Imports
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSetup {

    // Attributes
    private static final String SMTP_HOST = "send.one.com";
    private static final int SMTP_PORT = 465;
    private static final String OUR_EMAIL = "cupcake@travlr.dk";
    private static final String OUR_PASSWORD = "Nyepasswordssuttermax123!";

    // _________________________________________________

    private static Session createSession() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        props.put("mail.smtp.ssl.enable", "true"); // Important

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(OUR_EMAIL, OUR_PASSWORD);
            }
        });

    }

    // __________________________________________________________________

    public static boolean sendMail(String to, String subject, String body) {
        try {
            Session session = createSession();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(OUR_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

} // MailSetup end