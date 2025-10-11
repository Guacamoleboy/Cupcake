// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.entites.User;
import dk.cupcake.exceptions.DatabaseException;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.UserMapper;
import dk.cupcake.entites.Order;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;
import java.util.Map;

public class ValidationController {

    // Attributes
    private static final UserMapper userMapper = new UserMapper();

    // _______________________________________________

    public static void registerRoutes(Javalin app) {

        // POST /login
        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            try {
                User user = userMapper.login(username, password);
                ctx.sessionAttribute("user", user);
                ctx.redirect("/");
            } catch (DatabaseException e) {
                ctx.html(ThymeleafSetup.render("login.html", Map.of("error", e.getMessage())));
            }
        });

        // POST /register
        app.post("/register", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            String email = ctx.formParam("email");

            try {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.setRole("customer");

                userMapper.newUser(user);
                ctx.redirect("/login");
            } catch (DatabaseException e) {
                ctx.html(ThymeleafSetup.render("register.html", Map.of("error", e.getMessage())));
            }
        });

        // GET /ordertak/{id}
        app.get("/ordertak/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            OrderMapper orderMapper = new OrderMapper();
            Order order = orderMapper.getById(id);

            if (order == null) {
                ctx.status(404).html("");
                return;
            }

            ctx.html(ThymeleafSetup.render("tak-order.html", Map.of("order", order)));
        });

    }

} // ValidationController end