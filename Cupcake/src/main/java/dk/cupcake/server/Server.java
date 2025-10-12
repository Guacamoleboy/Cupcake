// Package
package dk.cupcake.server;

// Imports
import dk.cupcake.server.routing.Routing;
import io.javalin.Javalin;

public class Server {

    // Attributes
    private Javalin app;

    // _______________________________________________

    public void start(int port) {

        // Static Files
        app = Javalin.create(config -> {
            config.staticFiles.add("/static");
        }).start(port);

        // Routing
        Routing.registerRoutes(app);

        // Port print
        System.out.println("http://localhost: " + port);

    }

    // _______________________________________________

    public void stop() {
        if (app != null) app.stop();
    }

} // Server end