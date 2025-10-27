// Package
package dk.cupcake.server.routing;

// Imports
import dk.cupcake.controller.*;
import dk.cupcake.controller.Admin.AdminOrderController;
import dk.cupcake.controller.Admin.AdminUserController;
import dk.cupcake.controller.Profile.ProfileOrderController;
import dk.cupcake.controller.Profile.ProfileReturnController;
import io.javalin.Javalin;

public class Routing {

    // Attributes

    // _________________________________________________

    public static void registerRoutes(Javalin app) {

        PageController.registerRoutes(app);
        ProductController.registerRoutes(app);
        ValidationController.registerRoutes(app);
        UserController.registerRoutes(app);
        OrderController.registerRoutes(app);
        ContactController.registerRoutes(app);
        AccountController.registerRoutes(app);
        ProfileOrderController.registerRoutes(app);
        ProfileReturnController.registerRoutes(app);
        AdminOrderController.registerRoutes(app);
        AdminUserController.registerRoutes(app);

        // Last
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