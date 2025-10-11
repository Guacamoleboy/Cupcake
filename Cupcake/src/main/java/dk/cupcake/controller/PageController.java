// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;

public class PageController {

    // Attributes

    // _______________________________________________

    public static void registerRoutes(Javalin app) {

        app.get("/", ctx -> ctx.html(ThymeleafSetup.render("index.html", null)));
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
        app.get("/profile", ctx -> ctx.html(ThymeleafSetup.render("profile.html", null)));
        app.get("/register", ctx -> ctx.html(ThymeleafSetup.render("register.html", null)));
        app.get("/login", ctx -> ctx.html(ThymeleafSetup.render("login.html", null)));
        app.get("/tak", ctx -> ctx.html(ThymeleafSetup.render("tak.html", null)));

    }

}