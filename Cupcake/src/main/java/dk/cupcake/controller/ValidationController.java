// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.entities.User;
import dk.cupcake.exceptions.DatabaseException;
import dk.cupcake.mapper.UserMapper;
import io.javalin.Javalin;
import org.mindrot.jbcrypt.BCrypt;

public class ValidationController {

    // Attributes
    private static final UserMapper userMapper = new UserMapper();

    // ___________________________________________________

    public static void registerRoutes(Javalin app) {

        // POST /login
        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            if (username == null || password == null || username.isBlank() || password.isBlank()) {
                ctx.redirect("/login?error=missingFields");
                return;
            }

            try {
                User user = userMapper.getByUserName(username);

                if (user == null) {
                    ctx.redirect("/login?error=wrongInfo");
                    return;
                }

                // Check hashed password
                if (!BCrypt.checkpw(password, user.getPasswordHash())) {
                    ctx.redirect("/login?error=wrongInfo");
                    return;
                }

                ctx.sessionAttribute("user", user);
                ctx.redirect("/");

            } catch (Exception e) {
                ctx.redirect("/login?error=500");
            }
        });

        // __________________________________________________________________________________

        // POST /register
        app.post("/register", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            String passwordConfirm = ctx.formParam("passwordConfirm");
            String email = ctx.formParam("email");

            if (username == null || password == null || passwordConfirm == null || email == null ||
                username.isBlank() || password.isBlank() || passwordConfirm.isBlank() || email.isBlank()) {
                ctx.redirect("/register?error=missingFields");
                return;
            }

            if (!password.equals(passwordConfirm)) {
                ctx.redirect("/register?error=wrongPassMatch");
                return;
            }

            try {
                if (userMapper.existsByEmailOrUsername(email, username)) {
                    ctx.redirect("/register?error=accountExists");
                    return;
                }

                String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

                User user = new User();
                user.setUsername(username);
                user.setPasswordHash(hashed);
                user.setEmail(email);
                user.setRole("customer");

                userMapper.newUser(user);
                ctx.redirect("/login?error=accountCreated");

            } catch (DatabaseException e) {
                ctx.redirect("/register?error=500");
            } catch (Exception e) {
                ctx.redirect("/register?error=500");
            }

        });

    }

} // ValidationController end