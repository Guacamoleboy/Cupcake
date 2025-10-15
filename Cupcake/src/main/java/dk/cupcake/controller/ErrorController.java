// Package
package dk.cupcake.controller;

// Imports
import io.javalin.Javalin;
import io.javalin.http.Context;
import dk.cupcake.server.ThymeleafSetup;

public class ErrorController {

    // Attributes

    // ______________________________________________________________

    public static void registerErrorRoutes(Javalin app) {
        app.error(400, ErrorController::handle400);
        app.error(401, ErrorController::handle401);
        app.error(403, ErrorController::handle403);
        app.error(404, ErrorController::handle404);
        app.error(429, ErrorController::handle429);
        app.error(500, ErrorController::handle500);
        app.error(503, ErrorController::handle503);
    }

    // ______________________________________________________________

    public static void handle400(Context ctx) { // Bad Request (Invalid data or request)
        ctx.status(400);
        ctx.html(ThymeleafSetup.render("400.html", null));
    }

    // ______________________________________________________________

    public static void handle401(Context ctx) { // Unauthorized
        ctx.status(401);
        ctx.html(ThymeleafSetup.render("401.html", null));
    }

    // ______________________________________________________________

    public static void handle403(Context ctx) { // Forbidden (No Access)
        ctx.status(403);
        ctx.html(ThymeleafSetup.render("403.html", null));
    }

    // ______________________________________________________________

    public static void handle404(Context ctx) { // Not Found
        ctx.status(404);
        ctx.html(ThymeleafSetup.render("404.html", null));
    }

    // ______________________________________________________________

    public static void handle429(Context ctx) { // Rate Limit
        ctx.status(429);
        ctx.html(ThymeleafSetup.render("429.html", null));
    }

    // ______________________________________________________________

    public static void handle500(Context ctx) { // Server Code Error
        ctx.status(500);
        ctx.html(ThymeleafSetup.render("500.html", null));
    }

    // ______________________________________________________________

    public static void handle503(Context ctx) { // Server || Database -> Down
        ctx.status(503);
        ctx.html(ThymeleafSetup.render("503.html", null));
    }

} // ErrorController end