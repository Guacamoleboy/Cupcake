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

        // ______________________________________________________

        app.post("/admin/add-balance", ctx -> {

            String username = ctx.formParam("username");
            String idString = ctx.formParam("id");
            String amountString = ctx.formParam("amount");
            double amount;

            // Fixes our "Both Fields Entered" bug
            if ((username == null || username.isEmpty()) && (idString == null || idString.isEmpty())) {
                ctx.redirect("/admin?error=missingFields");
                return;
            }

            if (username != null && !username.isEmpty() && idString != null && !idString.isEmpty()) {
                ctx.redirect("/admin?error=onlyOneField");
                return;
            }

            if (amountString == null || amountString.isEmpty()) {
                ctx.redirect("/admin?error=missingAmount");
                return;
            }

            try {
                amount = Double.parseDouble(amountString);
            } catch (NumberFormatException e) {
                ctx.redirect("/admin?error=invalidAmount");
                return;
            }

            UserMapper userMapper = new UserMapper();

            try {
                if (idString != null && !idString.isEmpty()) {
                    int userId = Integer.parseInt(idString);
                    userMapper.addBalance(userId, amount);
                } else {
                    userMapper.addBalance(username, amount);
                }
                ctx.redirect("/admin?error=balanceAdded");
            } catch (SQLException e) {
                ctx.redirect("/admin?error=dbError");
            }
        });

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
        ctx.redirect("/login?error=emailIsReset");
    }


} // AccountController end