// Package
package dk.cupcake.server;

// Imports
import io.javalin.Javalin;
import java.util.Map;

public class Server {

    // Attributes
    private Javalin app;

    // _____________________________________________________

    public void start(int port) {

        // Start serveren
        app = Javalin.create(config -> {
            config.staticFiles.add("/static"); // folder i resources/static
        }).start(port);

        // Test af Thymeleaf i <h2> box...
        app.get("/", ctx -> {
            String html = ThymeleafSetup.render("index.html", Map.of("message", "Hej fra Thymeleaf!"));
            ctx.html(html);
        });

        System.out.println("http://localhost:" + port + " | I din URL bby girl");
    }

    // _____________________________________________________

    public void stop() {
        if (app != null) {
            app.stop();
        }
    }

} // Server End