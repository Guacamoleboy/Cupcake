// Package
package dk.cupcake.server.routing;

// Imports
import dk.cupcake.controller.*;
import dk.cupcake.entities.Order;
import io.javalin.Javalin;

public class Routing {

    // Attributes

    // _________________________________________________

    public static void registerRoutes(Javalin app) {

        PageController.registerRoutes(app);
        ProductController.registerRoutes(app);
        ValidationController.registerRoutes(app);
        UserController.registerRoutes(app);
        ErrorController.registerErrorRoutes(app);
        OrderController.registerErrorRoutes(app);

    }

    // _________________________________________________



} // Routing end