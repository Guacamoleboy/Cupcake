// Package
package dk.cupcake.server;

// Imports
import dk.cupcake.Order;
import dk.cupcake.mapper.OrderMapper;
import io.javalin.Javalin;
import java.util.Map;

public class Server {

    // Attributes
    private Javalin app;

    // _____________________________________________________

    public void start(int port) {

        // Resource folder
        app = Javalin.create(config -> {
            config.staticFiles.add("/static"); // folder i resources/static
        }).start(port);

        // Test af Thymeleaf i <h2> box...
        app.get("/", ctx -> {
            String html = ThymeleafSetup.render("index.html", null);
            ctx.html(html);
        });

        // Order Route

        app.get("/order", ctx -> {
            String html = ThymeleafSetup.render("order.html", null);
            ctx.html(html);
        });

        // Order Done With ID

        // :id is a generic value being input depending on what id has been collected

        app.get("/ordertak/{id}", ctx -> {

            // Allows any id to be accessed. If you were to write ordertak/69 without having access
            // It would most likely display it.
            int id = Integer.parseInt(ctx.pathParam("id"));

            // Our order mapper
            OrderMapper om = new OrderMapper();
            Order order = om.getById(id); // Adds our id from our url input.

            // If order isn't found then we are redicted to 404.html
            if (order == null) {
                ctx.status(404).html(""); // Blank since we're not sending any data.
                return;
            }

            // Thymeleaf render with variable named order.
            String html = ThymeleafSetup.render("tak-order.html", Map.of("order", order));
            ctx.html(html);

        });

        // Tak for besked ( /kontakt.html )
        app.get("/tak", ctx -> {
            String html = ThymeleafSetup.render("tak.html", null);
            ctx.html(html);
        });

        app.error(404, ctx -> {
            String html = ThymeleafSetup.render("404.html", null);
            ctx.html(html);
        });

        // Handles errors directly to 404.html

        System.out.println("http://localhost:" + port + " | I din URL bby girl");
    }

    // _____________________________________________________

    public void stop() {
        if (app != null) {
            app.stop();
        }
    }

} // Server End