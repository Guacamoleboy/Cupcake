// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.entities.Order;
import dk.cupcake.entities.Product;
import dk.cupcake.entities.User;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.ProductMapper;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PageController {

    // Attributes

    // _______________________________________________

    public static void registerRoutes(Javalin app) {

        // ______________________________________________________________

        app.get("/", ctx -> {

            String error = ctx.queryParam("error");

            if (error == null) {
                ctx.redirect("/?error=newMessage1");
                return;
            }

            ctx.html(ThymeleafSetup.render("index.html", null));
        });

        // ______________________________________________________________

        app.get("/api/auth/status", ctx -> {
            var user = ctx.sessionAttribute("user");

            if (user != null) {
                ctx.json(Map.of(
                        "loggedIn", true,
                        "username", ((User) user).getUsername(),
                        "role", ((User) user).getRole()
                ));
            } else {
                ctx.json(Map.of("loggedIn", false));
            }
        });

        // ______________________________________________________________

        app.get("/apply", ctx -> ctx.html(ThymeleafSetup.render("apply.html", null)));
        app.get("/carrer", ctx -> ctx.html(ThymeleafSetup.render("carrer.html", null)));
        app.get("/custom", ctx -> ctx.html(ThymeleafSetup.render("custom.html", null)));
        app.get("/events", ctx -> ctx.html(ThymeleafSetup.render("events.html", null)));
        app.get("/galleri", ctx -> ctx.html(ThymeleafSetup.render("galleri.html", null)));
        app.get("/jobs", ctx -> ctx.html(ThymeleafSetup.render("jobs.html", null)));

        // ______________________________________________________________

        app.get("/admin", ctx -> {

            User user = ctx.sessionAttribute("user");

            if (user == null) {
                ctx.redirect("/login");
                return;
            }

            ctx.html(ThymeleafSetup.render("admin.html", Map.of("user", user)));

        });


        // ______________________________________________________________

        app.get("/register", ctx -> ctx.html(ThymeleafSetup.render("register.html", null)));
        app.get("/login", ctx -> ctx.html(ThymeleafSetup.render("login.html", null)));

    }

} // PageController end