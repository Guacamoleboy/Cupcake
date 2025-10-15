// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.entities.Order;
import dk.cupcake.entities.OrderItem;
import dk.cupcake.entities.User;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;

import java.util.*;

public class OrderController {

    // Attributes
    static Order order;
    static OrderMapper orderMapper = new OrderMapper();

    // ______________________________________________________________

    public static void registerRoutes(Javalin app) {

        app.get("/tak", ctx -> ctx.html(ThymeleafSetup.render("tak.html", null)));
        app.get("/tak-ordre", ctx -> ctx.html(ThymeleafSetup.render("tak-order.html", null)));
        app.get("/payment", ctx -> ctx.html(ThymeleafSetup.render("payment.html", null)));
        app.get("/pay", ctx -> ctx.html(ThymeleafSetup.render("final-confirmation.html", null)));

        // ______________________________________________________________________________

        app.get("/ordertak/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            OrderMapper orderMapper = new OrderMapper();
            Order order = orderMapper.getById(id);

            if (order == null) {
                ctx.status(404).redirect("/?error=500");
                return;
            }

            ctx.render("tak-order.html", java.util.Map.of("order", order));

        });

        // ______________________________________________________________________________

        app.post("/cart/add", ctx -> {

            User user = ctx.sessionAttribute("user");

            order = ctx.sessionAttribute("order");

            if (order == null) {
                if (user != null) {
                    order = orderMapper.newOrder(user.getId());
                } else {
                    order = new Order();
                    order.setId(-1);
                }
            }

            int id = Integer.parseInt(ctx.formParam("id"));
            String name = ctx.formParam("name");
            double price = Double.parseDouble(ctx.formParam("price"));
            String description = ctx.formParam("description");
            int top = Integer.parseInt(ctx.formParam("topping"));
            int bottom = Integer.parseInt(ctx.formParam("bottom"));

            order.addToOrder(new OrderItem(id, name, description, price, 1, top, bottom), order.getId());

            ctx.sessionAttribute("order", order);

            double total = order.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();

            ctx.json(Map.of(
                    "items", order.getItems(),
                    "total", total
            ));

        });

        // ______________________________________________________________________________

        app.post("/cart/remove", ctx -> {

            User user = ctx.sessionAttribute("user");
            if(user == null) return;

            order = ctx.sessionAttribute("order");
            if (order == null) {
                order = orderMapper.newOrder(user.getId());
            }

            int id = Integer.parseInt(ctx.formParam("id"));
            int amount = Integer.parseInt(ctx.formParam("amount"));

            order.removeFromOrder(id, amount, order.getId());
            ctx.sessionAttribute("order", order);

            double total = order.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();

        });

        // ______________________________________________________________________________

        app.get("/cart/get", ctx -> {
            User user = ctx.sessionAttribute("user");

            if (user == null) {
                ctx.json(Map.of("items", Collections.emptyList(), "total", 0));
                return;
            }

            Order order = ctx.sessionAttribute("order");

            if (order == null) {
                order = new Order();
                ctx.sessionAttribute("order", order);
            }

            double total = order.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();

            ctx.json(Map.of(
                    "items", order.getItems(),
                    "total", total
            ));

        });

    }

} // OrderController end