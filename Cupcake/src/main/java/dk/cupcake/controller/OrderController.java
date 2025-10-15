package dk.cupcake.controller;

import dk.cupcake.entities.Order;
import dk.cupcake.entities.OrderItem;
import dk.cupcake.entities.User;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Map;

public class OrderController {
    static Order order;
    static OrderMapper orderMapper = new OrderMapper();
    public static void registerErrorRoutes(Javalin app) {
        app.get("/tak", ctx -> ctx.html(ThymeleafSetup.render("tak.html", null)));
        app.get("/tak-ordre", ctx -> ctx.html(ThymeleafSetup.render("tak-order.html", null)));
        app.get("/payment", ctx -> ctx.html(ThymeleafSetup.render("payment.html", null)));
        app.get("/pay", ctx -> ctx.html(ThymeleafSetup.render("final-confirmation.html", null)));


        // GET /ordertak/{id}
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

        app.post("/cart/add", ctx -> {

            User user = ctx.sessionAttribute("user");
            if(user == null) return;

            order = ctx.sessionAttribute("order");
            if (order == null) {
                order = orderMapper.newOrder(user.getId());
            }

            int id = Integer.parseInt(ctx.formParam("id"));
            String name = ctx.formParam("name");
            double price = Double.parseDouble(ctx.formParam("price"));
            String description = ctx.formParam("description");



            order.addToOrder(new OrderItem(id, name, description, price, 1), order.getId());
            ctx.sessionAttribute("order", order);

            double total = order.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();

            //TODO Total og orderItems skal så sendes til frontend, så det kan vises i kurven
            //TODO Backend virker!


            // Her kan du se kurven i backend!
            /*ArrayList<OrderItem> items = order.getItems();
            System.out.println("Liste over kurv:");
            int number = 1;
            for (OrderItem item : items) {
                System.out.println(number + ".) " + item.getTitle() + " x" + item.getQuantity());
                number++;
            }
            System.out.println("Total: " + total);*/
        });


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

            //TODO Vi skal have lavet en knap til at fjerne x antal af product
            //TODO (Tænker det er under kurven at der skal være + knap og - knap)
            //TODO Backend virker!

        });
    }
}
