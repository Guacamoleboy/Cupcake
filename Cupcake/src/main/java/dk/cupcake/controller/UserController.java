// Package
package dk.cupcake.controller;

// Imports
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.mindrot.jbcrypt.BCrypt;
import dk.cupcake.mapper.UserMapper;
import dk.cupcake.entities.User;
import dk.cupcake.exceptions.DatabaseException;

public class UserController {

    // Attributes
    private static UserMapper userMapper = new UserMapper();

    // ________________________________________________

    public static void registerRoutes(Javalin app) {
        app.post("/user/change-phone", changePhone);
        app.post("/user/change-username", changeUsername);
        app.post("/user/change-password", changePassword);
        app.post("/user/change-email", changeEmail);
        app.post("/user/delete-account", deleteAccount);
        app.get("/user/logout", logout);
    }

    // ________________________________________________

    public static Handler changePhone = ctx -> {

        int userId = ctx.sessionAttribute("userId");
        String newPhone = ctx.formParam("phone");

        if (newPhone == null || newPhone.isEmpty()) {
            ctx.redirect("/profile?error=missingPhone");
            return;
        }

        User user = userMapper.getById(userId);
        if (user.getPhone() == null) {
            user.setPhone("+ 45 XX XX XX XX");
        }
        user.setPhone(newPhone);

        try {
            userMapper.update(user);
            ctx.redirect("/profile?success=phoneUpdated");
        } catch (Exception e) {
            ctx.redirect("/profile?error=500");
        }

    };

    // ________________________________________________

    public static Handler changeUsername = ctx -> {

        String oldUsername = ctx.formParam("oldUsername");
        String newUsername = ctx.formParam("newUsername");
        String password = ctx.formParam("password");

        if (oldUsername == null || newUsername == null || password == null) {
            ctx.redirect("/profile?error=missingFields");
            return;
        }

        User user = userMapper.getByUserName(oldUsername);

        if (user == null) {
            ctx.redirect("/profile?error=userNotFound");
            return;
        }

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            ctx.redirect("/profile?error=wrongPassword");
            return;
        }

        user.setUsername(newUsername);

        try {
            userMapper.update(user);
            ctx.redirect("/login?error=usernameChanged");
        } catch (Exception e) {
            ctx.redirect("/profile?error=500");
        }
    };

    // ________________________________________________

    public static Handler changePassword = ctx -> {

        String username = ctx.formParam("username");
        String oldPassword = ctx.formParam("oldPassword");
        String newPassword = ctx.formParam("newPassword");
        String newPasswordRepeat = ctx.formParam("newPasswordRepeat");

        if (username == null || oldPassword == null || newPassword == null || newPasswordRepeat == null) {
            ctx.redirect("/profile?error=missingFields");
            return;
        }

        if (!newPassword.equals(newPasswordRepeat)) {
            ctx.redirect("/profile?error=passwordsDontMatch");
            return;
        }

        User user = userMapper.getByUserName(username);
        if (user == null) {
            ctx.redirect("/profile?error=userNotFound");
            return;
        }

        if (!BCrypt.checkpw(oldPassword, user.getPasswordHash())) {
            ctx.redirect("/profile?error=wrongPassword");
            return;
        }

        user.setPasswordHash(BCrypt.hashpw(newPassword, BCrypt.gensalt()));

        try {
            userMapper.update(user);
            ctx.redirect("/login?success=passwordChanged");
        } catch (Exception e) {
            ctx.redirect("/profile?error=500");
        }
    };

    // ________________________________________________

    public static Handler changeEmail = ctx -> {

        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        String oldEmail = ctx.formParam("oldEmail");
        String newEmail = ctx.formParam("newEmail");

        if (username == null || password == null || oldEmail == null || newEmail == null) {
            ctx.redirect("/profile?error=missingFields");
            return;
        }

        User user = userMapper.getByUserName(username);
        if (user == null) {
            ctx.redirect("/profile?error=userNotFound");
            return;
        }

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            ctx.redirect("/profile?error=wrongPassword");
            return;
        }

        if (!user.getEmail().equals(oldEmail)) {
            ctx.redirect("/profile?error=wrongOldEmail");
            return;
        }

        user.setEmail(newEmail);

        try {
            userMapper.update(user);
            ctx.redirect("/login?success=emailChanged");
        } catch (Exception e) {
            ctx.redirect("/profile?error=500");
        }
    };

    // ________________________________________________

    public static Handler deleteAccount = ctx -> {

        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        String email = ctx.formParam("email");
        String confirm = ctx.formParam("confirm");

        if (username == null || password == null || email == null || confirm == null) {
            ctx.redirect("/profile?error=missingFields");
            return;
        }

        if (!confirm.equalsIgnoreCase("ja")) {
            ctx.redirect("/profile?error=notConfirmed");
            return;
        }

        User user = userMapper.getByUserName(username);
        if (user == null) {
            ctx.redirect("/profile?error=userNotFound");
            return;
        }

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            ctx.redirect("/profile?error=wrongPassword");
            return;
        }

        if (!user.getEmail().equals(email)) {
            ctx.redirect("/profile?error=wrongEmail");
            return;
        }

        try {
            userMapper.delete(user.getId());
            ctx.redirect("/?success=accountDeleted");
        } catch (Exception e) {
            ctx.redirect("/profile?error=500");
        }
    };

    // ________________________________________________

    public static Handler logout = ctx -> {

        ctx.sessionAttribute("user", null);
        ctx.redirect("/");

    };

} // UserController end