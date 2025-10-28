package dk.cupcake.controller.Profile;

import dk.cupcake.entities.Order;
import dk.cupcake.entities.User;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProfileOrderController {

    // Attributes
    static OrderMapper orderMapper = new OrderMapper();

    // ______________________________________________________________

    public static void registerRoutes(Javalin app) {

        ProfileOrderController controller = new ProfileOrderController();

        app.get("/profile", controller::showProfile);
        app.get("/removeOrder", controller::removeOrder);
        app.post("/searchOrder", controller::searchOrder);

    }

    // ______________________________________________________________

    public void showProfile(Context ctx) throws SQLException {

        User user = ctx.sessionAttribute("user");
        OrderMapper orderMapper = new OrderMapper();
        if (user == null) {
            ctx.redirect("/login");
            return;
        }


        List<Order> orders = orderMapper.getByUserId(user.getId());
        orders.forEach(o -> {
            double total = o.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();
            o.setTotalPrice(total);
        });


        ctx.html(ThymeleafSetup.render("profile.html", Map.of("user", user, "orders", orders)));
    }

    // ______________________________________________________________

    public void removeOrder(Context ctx) throws SQLException {

        String orderIdParam = ctx.queryParam("orderId");
        OrderMapper orderMapper = new OrderMapper();
        orderMapper.delete(Integer.parseInt(orderIdParam));
        ctx.redirect("/profile#mineOrdre");

    }

    // ______________________________________________________________

    public void searchOrder(Context ctx) {
        User user = ctx.sessionAttribute("user");

        if (user == null) {

            ctx.status(401).json(Map.of("error", "Du skal være logget ind"));
            return;

        }

        try {

            String orderIdParam = ctx.formParam("orderId");
            if (orderIdParam == null || orderIdParam.isBlank()) {

                ctx.status(400).json(Map.of("error", "Ordre ID mangler"));
                return;

            }

            int orderId = Integer.parseInt(orderIdParam);
            Order order = orderMapper.getUserOrderByID(orderId, user.getId());

            if (order == null) {

                ctx.status(404).json(Map.of("error", "Ordre ikke fundet"));
                return;

            }

            double total = order.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();
            order.setTotalPrice(total);

            ctx.json(order);

        } catch (NumberFormatException e) {

            ctx.status(400).json(Map.of("error", "Ugyldigt ID"));

        } catch (Exception e) {

            ctx.status(500).json(Map.of("error", "Server fejl"));

        }
    }

}