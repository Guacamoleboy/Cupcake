package dk.cupcake.controller.Admin;

import dk.cupcake.entities.User;
import dk.cupcake.exceptions.DatabaseException;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.RefundMapper;
import dk.cupcake.mapper.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Map;

public class AdminUserController {

    private static final UserMapper userMapper = new UserMapper();
    private static final OrderMapper orderMapper = new OrderMapper();
    private static final RefundMapper refundMapper = new RefundMapper();

    // ______________________________________________________

    public static void registerRoutes(Javalin app) {

        AdminUserController controller = new AdminUserController();

        app.post("/admin/searchUser", controller::searchUser);
        app.post("/admin/createUser", controller::createUser);
        app.post("/admin/deleteUser", controller::deleteUser);

    }

    // ______________________________________________________

    public void searchUser(Context ctx) throws SQLException {

        String idParam = ctx.formParam("id");
        String username = ctx.formParam("username");
        String email = ctx.formParam("email");

        User user = null;

        if (idParam != null && !idParam.isBlank()) {

            user = userMapper.getById(Integer.parseInt(idParam));

        } else if (username != null && !username.isBlank()) {

            user = userMapper.getByUserName(username);

        } else if (email != null && !email.isBlank()) {

            user = userMapper.getByEmail(email);

        }

        if (user == null) {

            ctx.status(404).json(Map.of("error", "Ingen bruger fundet"));
            return;

        }


        ctx.json(user);

    }

    // ______________________________________________________

    public void createUser(Context ctx) {
        String password = ctx.formParam("password");
        String username = ctx.formParam("username");
        String email = ctx.formParam("email");
        if (username == null || password == null || email == null ||
                username.isBlank() || password.isBlank() || email.isBlank()) {
            ctx.redirect("/Admin?error=missingFields");
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

            ctx.json(user);

        } catch (DatabaseException e) {
            ctx.redirect("/register?error=500");
        } catch (Exception e) {
            ctx.redirect("/register?error=500");
        }
    }

    // ______________________________________________________

    public void deleteUser(Context ctx) throws SQLException {
        System.out.println("Modtager jeg?");
        String idParam = ctx.formParam("id");
        String username = ctx.formParam("username");
        String email = ctx.formParam("email");

        User user = null;

        if (idParam != null && !idParam.isBlank()) {

            user = userMapper.getById(Integer.parseInt(idParam));

        } else if (username != null && !username.isBlank()) {

            user = userMapper.getByUserName(username);

        } else if (email != null && !email.isBlank()) {

            user = userMapper.getByEmail(email);

        }

        if (user == null) {

            ctx.status(404).json(Map.of("error", "Ingen bruger fundet"));
            return;

        }

        ctx.json(user);

        userMapper.delete(user.getId());
    }

}
