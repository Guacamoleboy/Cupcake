// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.entities.User;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;

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

        app.get("/admin", ctx -> ctx.html(ThymeleafSetup.render("admin.html", null)));
        app.get("/apply", ctx -> ctx.html(ThymeleafSetup.render("apply.html", null)));
        app.get("/carrer", ctx -> ctx.html(ThymeleafSetup.render("carrer.html", null)));
        app.get("/contact", ctx -> ctx.html(ThymeleafSetup.render("contact.html", null)));
        app.get("/custom", ctx -> ctx.html(ThymeleafSetup.render("custom.html", null)));
        app.get("/events", ctx -> ctx.html(ThymeleafSetup.render("events.html", null)));
        app.get("/galleri", ctx -> ctx.html(ThymeleafSetup.render("galleri.html", null)));
        app.get("/jobs", ctx -> ctx.html(ThymeleafSetup.render("jobs.html", null)));
        app.get("/order", ctx -> ctx.html(ThymeleafSetup.render("order.html", null)));
        app.get("/payment", ctx -> ctx.html(ThymeleafSetup.render("payment.html", null)));
        app.get("/pay", ctx -> ctx.html(ThymeleafSetup.render("final-confirmation.html", null)));

        // ______________________________________________________________

        app.get("/profile", ctx -> {

            User user = ctx.sessionAttribute("user");

            if (user == null) {
                ctx.redirect("/login");
                return;
            }

            ctx.html(ThymeleafSetup.render("profile.html", Map.of("user", user)));

        });

        // ______________________________________________________________

        app.get("/register", ctx -> ctx.html(ThymeleafSetup.render("register.html", null)));
        app.get("/login", ctx -> ctx.html(ThymeleafSetup.render("login.html", null)));
        app.get("/tak", ctx -> ctx.html(ThymeleafSetup.render("tak.html", null)));
        app.get("/tak-ordre", ctx -> ctx.html(ThymeleafSetup.render("tak-order.html", null)));

    }

} // PageController end