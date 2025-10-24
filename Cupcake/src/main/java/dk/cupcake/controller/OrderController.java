// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.Coupon;
import dk.cupcake.entities.Order;
import dk.cupcake.entities.OrderItem;
import dk.cupcake.entities.User;
import dk.cupcake.mapper.CouponMapper;
import dk.cupcake.mapper.CupcakeFlavorMapper;
import dk.cupcake.mapper.CupcakeToppingMapper;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;

import java.util.*;

public class OrderController {

    // Attributes
    static Order order;
    static OrderMapper orderMapper = new OrderMapper();
    static CupcakeToppingMapper toppingMapper = new CupcakeToppingMapper();
    static CupcakeFlavorMapper bottomMapper = new CupcakeFlavorMapper();

    // ______________________________________________________________

    public static void registerRoutes(Javalin app) {

        app.get("/tak-ordre", ctx -> ctx.html(ThymeleafSetup.render("tak-order.html", null)));

        // ______________________________________________________________________________

        app.get("/ordertak/{id}", ctx -> {
            order = ctx.sessionAttribute("order");
            order.setStatus("closed");
            orderMapper.updateOrderStatus(order);
            if (order == null) {
                ctx.status(404).redirect("/?error=500");
                return;
            }

            ctx.html(ThymeleafSetup.render("tak-order.html", java.util.Map.of("order", order)));

            ctx.sessionAttribute("order", null);
            order = null;
        });

        // ______________________________________________________________________________

        app.post("/cart/add", ctx -> {
            User user = ctx.sessionAttribute("user");

            order = ctx.sessionAttribute("order");

            if (order == null) {
                if (user != null) {
                    order = orderMapper.newOrder(user.getId());
                } else {
                    order = orderMapper.newOrder(0);
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

            order.setTotalPrice(total);
            ctx.sessionAttribute("total", total);

            ctx.json(Map.of(
                    "items", order.getItems(),
                    "total", total
            ));

        });

        // ______________________________________________________________________________

        app.post("/cart/remove", ctx -> {
            User user = ctx.sessionAttribute("user");

            order = ctx.sessionAttribute("order");

            /* TODO Fjern det her fis. Brugeren har ikke behov for at se sit ID før der er placeret en ordre. */
            if (order == null) {
                if (user != null) {
                    order = orderMapper.newOrder(user.getId());
                } else {
                    order = orderMapper.newOrder(0);
                }
            }

            int id = Integer.parseInt(ctx.formParam("id"));
            int amount = Integer.parseInt(ctx.formParam("amount"));

            OrderItem item = order.getItems().stream()
                    .filter(i -> i.getProductId() == id)
                    .findFirst()
                    .orElse(null);

            if (item == null) {
                ctx.status(400).result("Item findes ikke i kurven");
                return;
            }

            if (amount > item.getQuantity()) {
                amount = item.getQuantity();
            }

            order.removeFromOrder(id, amount, order.getId());

            ctx.sessionAttribute("order", order);

            double total = order.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();

            order.setTotalPrice(total);
            ctx.sessionAttribute("total", total);

            ctx.json(Map.of(
                    "items", order.getItems(),
                    "total", total
            ));
        });

        // ______________________________________________________________________________

        app.get("/cart/get", ctx -> {
            User user = ctx.sessionAttribute("user");

            order = ctx.sessionAttribute("order");

            /* TODO Fjern det her fis. Brugeren har ikke behov for at se sit ID før der er placeret en ordre. */
            if (order == null) {
                if (user != null) {
                    order = orderMapper.newOrder(user.getId());
                } else {
                    order = orderMapper.newOrder(0);
                }
            }

            double total = order.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();
            order.setTotalPrice(total);
            ctx.sessionAttribute("total", total);

            ctx.json(Map.of(
                    "items", order.getItems(),
                    "total", total
            ));

        });

        // ______________________________________________________________________________

        app.get("/payment", ctx -> {
            User user = ctx.sessionAttribute("user");
            Order order = ctx.sessionAttribute("order");

            if (order == null || order.getItems().isEmpty()) {
                ctx.redirect("/?error=emptyCart");
                return;
            }

            if (order.getId() <= 0 && user != null) {
                order = orderMapper.newOrder(user.getId());
                ctx.sessionAttribute("order", order);
            }

            double total = order.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();
            order.setTotalPrice(total);

            ctx.sessionAttribute("total", total);

            ctx.html(ThymeleafSetup.render("payment.html", Map.of("order", order)));
        });

        // ______________________________________________________________________________

        app.post("/apply-coupon", ctx -> {

            String code = ctx.formParam("couponCode");
            CouponMapper couponMapper = new CouponMapper();
            Coupon coupon = couponMapper.getCouponByCode(code);

            if (coupon != null) {

                Order order = ctx.sessionAttribute("order");

                if (order != null) {

                    double total = order.getItems().stream()
                            .mapToDouble(i -> i.getPrice() * i.getQuantity())
                            .sum();
                    double discountedTotal = total * (1 - coupon.getDiscountPercent() / 100.0);

                    ctx.sessionAttribute("coupon", coupon);
                    ctx.sessionAttribute("discountedTotal", discountedTotal);
                    ctx.json(Map.of(
                            "success", true,
                            "discountPercent", coupon.getDiscountPercent(),
                            "discountedTotal", discountedTotal
                    ));

                }

            } else {

                ctx.json(Map.of(
                        "success", false,
                        "message", "Ugyldig rabatkode eller udløbet"
                ));

            }
        });

        // ______________________________________________________________________________

        app.post("/update-payment-info", ctx -> {
            String deliveryMethod = ctx.formParam("deliveryMethod");
            String paymentMethod = ctx.formParam("paymentMethod");

            ctx.sessionAttribute("deliveryMethod", deliveryMethod);
            ctx.sessionAttribute("paymentMethod", paymentMethod);
            ctx.json(Map.of("success", true));
        });

        // ______________________________________________________________________________

        app.get("/pay", ctx -> {

            Order order = ctx.sessionAttribute("order");

            if (order == null || order.getItems().isEmpty()) {
                ctx.redirect("/?error=emptyCart");
                return;
            }

            User user = ctx.sessionAttribute("user");

            List<OrderItem> cartItems = order.getItems();
            Double originalTotal = ctx.sessionAttribute("total");
            Double discountedTotal = ctx.sessionAttribute("discountedTotal");
            String deliveryMethod = ctx.sessionAttribute("deliveryMethod");
            String paymentMethod = ctx.sessionAttribute("paymentMethod");
            Coupon coupon = ctx.sessionAttribute("coupon");

            Map<String, Object> model = new HashMap<>();
            model.put("order", order);
            model.put("cartItems", cartItems);
            model.put("originalTotal", originalTotal);
            model.put("discountedTotal", discountedTotal);
            model.put("deliveryMethod", deliveryMethod);
            model.put("paymentMethod", paymentMethod);
            model.put("coupon", coupon);
            model.put("user", user);

            ctx.html(ThymeleafSetup.render("final-confirmation.html", Map.of("data", model)));

        });

    }

} // OrderController end