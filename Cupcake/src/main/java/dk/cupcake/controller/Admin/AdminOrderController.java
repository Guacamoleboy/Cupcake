package dk.cupcake.controller.Admin;

import dk.cupcake.entities.Order;
import dk.cupcake.entities.Refund;
import dk.cupcake.entities.User;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.RefundMapper;
import dk.cupcake.mapper.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminOrderController {

    private static final UserMapper userMapper = new UserMapper();
    private static final OrderMapper orderMapper = new OrderMapper();
    private static final RefundMapper refundMapper = new RefundMapper();

    // ______________________________________________________

    public static void registerRoutes(Javalin app) {

        AdminOrderController controller = new AdminOrderController();

        app.post("/admin/searchOrder", controller::searchOrdersForUser);
        app.post("/admin/searchOrderID", controller::searchOrderID);
        app.post("/admin/manageOrder", controller::manageOrder);
        app.post("/admin/showOrders", controller::showOrders);
        //app.post("/admin/createOrder", controller::createOrder);
        app.post("/admin/manageRefusion", controller::manageRefusion);
        app.post("/admin/returnOrder", controller::returnOrder);
        app.post("/admin/returnOrderShowAll", controller::returnOrderShowAll);

    }

    // ______________________________________________________

    public void searchOrdersForUser(Context ctx) throws SQLException {

        String idParam = ctx.formParam("userId");
        String username = ctx.formParam("username");
        String email = ctx.formParam("email");

        User user = null;

        if (idParam != null && !idParam.isBlank()) {

            user = userMapper.getById(Integer.parseInt(idParam));

        } else if (username != null && !username.isBlank()) {

            user = userMapper.getByUserName(username);

        } else if (email != null && !email.isBlank()) {

            user = userMapper.getByEmail(email);

        }

        if (user == null) {

            ctx.status(404).json(Map.of("error", "Ingen bruger fundet"));
            return;

        }

        List<Order> orders = orderMapper.getByUserId(user.getId());
        orders.forEach(o -> {

            double total = o.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();

            o.setTotalPrice(total);

        });

        ctx.json(orders);
    }

    // ______________________________________________________

    public void searchOrderID(Context ctx) {

        try {

            String orderIdParam = ctx.formParam("orderId");
            if (orderIdParam == null || orderIdParam.isBlank()) {

                ctx.status(400).json(Map.of("error", "Ordre ID mangler"));
                return;

            }

            int orderId = Integer.parseInt(orderIdParam);
            Order order = orderMapper.getById(orderId);

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

            ctx.status(400).json(Map.of("error", "Ugyldigt user id eller ordre nummer"));

        } catch (Exception e) {

            ctx.status(500).json(Map.of("error", "Server fejl"));

        }
    }

    // ______________________________________________________

    public void manageOrder(Context ctx) {

        //TODO Jeg laver denne når jeg ikke er helt smadret - Rovelt123456789

        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        boolean delete = "true".equals(ctx.formParam("delete"));
        String itemsJson = ctx.formParam("items");
        System.out.println(orderId);
        System.out.println(itemsJson);

        if (delete) {

            System.out.println("Slet her!");

        }

        System.out.println("Backend her!");
    }

    // ______________________________________________________

    public void showOrders(Context ctx) throws SQLException {

        List<Order> orders = orderMapper.getAll();

        List<Order> filteredOrders = orders.stream()
                .filter(order -> order.getItems() != null && !order.getItems().isEmpty())
                .collect(Collectors.toList());

        ctx.json(filteredOrders);

    }

    // ______________________________________________________

    /*public void createOrder(Context ctx) {
        Er denne nødvendig????
    }*/

    // ______________________________________________________

    public void manageRefusion(Context ctx) {

        //TODO Jeg laver denne når jeg ikke er helt smadret - Rovelt123456789

    }

    // ______________________________________________________

    public void returnOrder(Context ctx) {

        //TODO Skal lige laves så man får penge tilbage også

        try {

            String action = ctx.formParam("action");
            String returnIdParam = ctx.formParam("returnId");

            if (returnIdParam == null) {

                ctx.json(refundMapper.getAllActiveRefunds());
                return;

            }

            int returnId = Integer.parseInt(returnIdParam);

            if (action == null) {

                ctx.json(List.of(refundMapper.getRefundByID(returnId)));
                return;

            }

            if ("confirm".equals(action)) {

                refundMapper.updateRefundStatus(returnId, "returned");

            } else if ("reject".equals(action)) {

                refundMapper.updateRefundStatus(returnId, "closed");

            }

            ctx.json(Map.of("success", "Status opdateret"));

        } catch (Exception e) {

            ctx.status(500).json(Map.of("error", e.getMessage()));

        }
    }

    // ______________________________________________________

    public void returnOrderShowAll(Context ctx) throws SQLException {
        List<Refund> refunds = refundMapper.getAllRefunds();

        ctx.json(refunds);
    }


}
