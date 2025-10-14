// Package
package dk.cupcake.server.routing;

// Imports
import dk.cupcake.controller.ErrorController;
import dk.cupcake.controller.PageController;
import dk.cupcake.controller.UserController;
import dk.cupcake.controller.ValidationController;
import io.javalin.Javalin;

public class Routing {

    // Attributes

    // _________________________________________________

    public static void registerRoutes(Javalin app) {

        PageController.registerRoutes(app);
        ValidationController.registerRoutes(app);
        UserController.registerRoutes(app);
        registerErrorRoutes(app);

    }

    // _________________________________________________

    private static void registerErrorRoutes(Javalin app) {
        app.error(400, ErrorController::handle400);
        app.error(401, ErrorController::handle401);
        app.error(403, ErrorController::handle403);
        app.error(404, ErrorController::handle404);
        app.error(429, ErrorController::handle429);
        app.error(500, ErrorController::handle500);
        app.error(503, ErrorController::handle503);
    }

} // Routing end