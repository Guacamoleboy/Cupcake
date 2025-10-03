// Package
package dk.cupcake.server;

// Imports
import dk.cupcake.exceptions.DatabaseException;
import dk.cupcake.entites.Order;
import dk.cupcake.entites.User;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.UserMapper;
import io.javalin.Javalin;
import java.util.Map;

public class Server {

    // Attributes
    private Javalin app;
    private UserMapper userMapper;

    // _____________________________________________________

    public void start(int port) {
        userMapper = new UserMapper();
        // Resource folder
        app = Javalin.create(config -> {
            config.staticFiles.add("/static"); // folder i resources/static
        }).start(port);

        createRoutes();
        // Handles errors directly to 404.html

        System.out.println("http://localhost:" + port + " | I din URL bby girl");
    }

    // _____________________________________________________

    public void stop() {
        if (app != null) {
            app.stop();
        }
    }

    // _____________________________________________________

    public void createRoutes() {

        // 404 siden
        app.error(404, ctx -> {
            String html = ThymeleafSetup.render("404.html", null);
            ctx.html(html);
        });


        // Admin siden
        app.get("/admin", ctx -> {
            String html = ThymeleafSetup.render("admin.html", null);
            ctx.html(html);
        });


        // Apply siden
        app.get("/apply", ctx -> {
            String html = ThymeleafSetup.render("apply.html", null);
            ctx.html(html);
        });


        // Carrer siden
        app.get("/carrer", ctx -> {
            String html = ThymeleafSetup.render("carrer.html", null);
            ctx.html(html);
        });


        // Contact siden
        app.get("/contact", ctx -> {
            String html = ThymeleafSetup.render("contact.html", null);
            ctx.html(html);
        });


        // Customer siden
        app.get("/customer", ctx -> {
            String html = ThymeleafSetup.render("customer.html", null);
            ctx.html(html);
        });


        // Events siden
        app.get("/events", ctx -> {
            String html = ThymeleafSetup.render("events.html", null);
            ctx.html(html);
        });


        // Gallery siden
        app.get("/galleri", ctx -> {
            String html = ThymeleafSetup.render("galleri.html", null);
            ctx.html(html);
        });


        // Homepage siden
        app.get("/", ctx -> {
            String html = ThymeleafSetup.render("index.html", null);
            ctx.html(html);
        });


        // Jobs siden
        app.get("/jobs", ctx -> {
            String html = ThymeleafSetup.render("jobs.html", null);
            ctx.html(html);
        });


        // Login form (Viser login siden)
        app.get("/login", ctx -> {
            String html = ThymeleafSetup.render("login.html", null);
            ctx.html(html);
        });

        // Login submit (Verificerer om login er korrekt)
        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            try {
                User user = userMapper.login(username, password);
                ctx.sessionAttribute("user", user);
                ctx.redirect("/");
            } catch (DatabaseException e) {
                // Sender fejlbesked til Thymeleaf
                String html = ThymeleafSetup.render("login.html", Map.of("error", e.getMessage()));
                ctx.html(html);
            }
        });


        // Order siden
        app.get("/order", ctx -> {
            String html = ThymeleafSetup.render("order.html", null);
            ctx.html(html);
        });


        // Payment siden
        app.get("/payment", ctx -> {
            String html = ThymeleafSetup.render("payment.html", null);
            ctx.html(html);
        });


        // Profile siden
        app.get("/profile", ctx -> {
            String html = ThymeleafSetup.render("profile.html", null);
            ctx.html(html);
        });


        // Register form (Viser register siden)
        app.get("/register", ctx -> {
            String html = ThymeleafSetup.render("register.html", null);
            ctx.html(html);
        });

        // Register (Oprettelse af ny bruger)
        app.post("/register", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            String email = ctx.formParam("email");

            try {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.setRole("HVAD-ER-DETTE(???)");
                userMapper.newUser(user);
                ctx.redirect("/login");
            } catch (DatabaseException e) {
                // Sender fejlbesked til Thymeleaf
                String html = ThymeleafSetup.render("register.html", Map.of("error", e.getMessage()));
                ctx.html(html);
            }
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
    }

} // Server End