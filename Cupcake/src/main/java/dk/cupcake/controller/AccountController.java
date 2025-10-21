// Packages
package dk.cupcake.controller;

// Imports
import dk.cupcake.entities.User;
import dk.cupcake.mapper.UserMapper;
import dk.cupcake.server.MailSetup;
import dk.cupcake.server.ThymeleafSetup;
import dk.cupcake.server.TokenGenerator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.Map;

public class AccountController {

    // Attributes
    private final UserMapper userMapper = new UserMapper();

    // ______________________________________________________

    public static void registerRoutes(Javalin app) {

        AccountController controller = new AccountController();

        app.post("/forgot/password", controller::forgotPassword);
        app.post("/forgot/email", controller::forgotEmail);
        app.get("/forgot/resetPassword", ctx -> { ctx.html(ThymeleafSetup.render("passwordhash.html", null));});
        app.post("/forgot/resetPassword", controller::handleResetPassword);

    }

    // ______________________________________________________

    public void handleResetPassword(Context ctx) {

        String token = ctx.formParam("token");
        String newPassword = ctx.formParam("newPassword");
        String confirmPassword = ctx.formParam("confirmPassword");

        if (token == null || newPassword == null || confirmPassword == null ||
                token.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            ctx.redirect("/forgot/resetPassword?error=missingFields");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            ctx.redirect("/forgot/resetPassword?error=wrongPassMatch");
            return;
        }

        if (!TokenGenerator.isValidToken(token)) {
            ctx.redirect("/forgot/resetPassword?error=invalidToken");
            return;
        }

        try {
            int userId = TokenGenerator.getUserIdByToken(token);
            if (userId == -1) {
                ctx.redirect("/forgot/resetPassword?error=userNotFound");
                return;
            }

            User user = userMapper.getById(userId);
            if (user == null) {
                ctx.redirect("/forgot/resetPassword?error=userNotFound");
                return;
            }

            System.out.println("Updating user with ID: " + user.getId());

            String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPasswordHash(hashed);
            userMapper.update(user);

            TokenGenerator.invalidateToken(token);
            ctx.redirect("/login?error=passwordReset");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.redirect("/forgot/resetPassword?error=dbError");
        }
    }

    // ______________________________________________________

    public void forgotPassword(Context ctx) {

        String email = ctx.formParam("email");

        try {
            User user = userMapper.getByEmail(email);

            if (user == null) {
                ctx.redirect("/forgot?error=userNotFound");
                return;
            }

            String token = TokenGenerator.generateResetToken(user.getId());
            String body = "Hej!\n\nHer er din kode til nulstilling:\n\n" + token + "\nDette udløber om 5 minutter.";

            boolean sent = MailSetup.sendMail(email, "Nulstil adgangskode", body);
            if (sent) {
                ctx.redirect("/forgot/resetPassword?error=passwordResetSent");
            } else {
                ctx.redirect("/forgot?error=contactError");
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.redirect("/forgot?error=dbError");
        }
    }

    // ______________________________________________________

    public void forgotEmail(Context ctx) {
        // TODO her skal man kunne se sin mail. Dvs man skriver Password + Username, og så displayer den din mail
        ctx.redirect("/login?error=emailIsReset");
    }

} // AccountController end