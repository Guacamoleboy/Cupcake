package dk.cupcake.controller.Admin;

import dk.cupcake.entities.Order;
import dk.cupcake.entities.OrderItem;
import dk.cupcake.entities.Refund;
import dk.cupcake.entities.User;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.RefundMapper;
import dk.cupcake.mapper.OrderItemMapper;
import dk.cupcake.mapper.ProductMapper;
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
        try {
            String orderIdParam = ctx.formParam("orderId");
            if (orderIdParam == null) {
                ctx.redirect("/admin?error=invalidParams#updateDeleteOrderAdmin");
                return;
            }
            int orderId = Integer.parseInt(orderIdParam);

            if ("true".equals(ctx.formParam("delete"))) {
                orderMapper.delete(orderId);
                String anchor = ctx.formParam("anchor");
                if (anchor == null || anchor.isBlank()) anchor = "updateDeleteOrderAdmin";
                ctx.redirect("/admin?error=orderDeleted#" + anchor);
                return;
            }

            Map<String, List<String>> form = ctx.formParamMap();
            List<String> titles = form.get("title");
            List<String> quantities = form.get("quantity");
            List<String> prices = form.get("price");

            if (titles == null || quantities == null || prices == null ||
                titles.size() != quantities.size() || titles.size() != prices.size()) 
            {
                ctx.redirect("/admin?error=invalidParams#updateDeleteOrderAdmin");
                return;
            }

            Order order = orderMapper.getById(orderId);
            if (order == null) {
                ctx.redirect("/admin?error=noOrderFound#updateDeleteOrderAdmin");
                return;
            }

            List<OrderItem> items = order.getItems();
            java.util.Set<String> kept = new java.util.HashSet<>();
            ProductMapper productMapper = new ProductMapper();

            for (int i = 0; i < titles.size(); i++) {
                String title = String.valueOf(titles.get(i));
                int quantity;
                double price;
                try {
                    quantity = Integer.parseInt(String.valueOf(quantities.get(i)));
                    price = Double.parseDouble(String.valueOf(prices.get(i)));
                } catch (Exception ignore) {
                    continue;
                }
                if (quantity <= 0) quantity = 1;

                OrderItem match = items.stream()
                .filter(oi -> title.equals(oi.getTitle()))
                .findFirst()
                .orElse(null);

                if (match == null) continue;

                kept.add(title);
                OrderItemMapper.updateQuantity(order.getId(), match.getId(), quantity);

                if (Double.compare(price, match.getPrice()) != 0) {
                    var product = productMapper.getById(match.getId());
                    if (product != null) {
                        product.setPrice(price);
                        productMapper.update(product);
                    }
                }
            }

            for (OrderItem oi : items) {
                if (!kept.contains(oi.getTitle())) {
                    OrderItemMapper.deleteOrderItem(order.getId(), oi.getId());
                }
            }

            ctx.redirect("/admin?error=orderUpdated#updateDeleteOrderAdmin");
        } catch (NumberFormatException e) {
            ctx.redirect("/admin?error=invalidParams#updateDeleteOrderAdmin");
        } catch (Exception e) {
            ctx.redirect("/admin?error=dbError#updateDeleteOrderAdmin");
        }
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

    public void returnOrder(Context ctx) {

        try {

            String action = ctx.formParam("action");
            String returnIdParam = ctx.formParam("returnId");

            if (returnIdParam == null) {
                ctx.redirect("/admin?error=invalidParams#returnAdmin");
                return;
            }

            int returnId = Integer.parseInt(returnIdParam);

            if (action == null) {
                ctx.redirect("/admin?error=invalidParams#returnAdmin");
                return;
            }

            if ("confirm".equals(action)) {

                Refund refund = refundMapper.getRefundByID(returnId);
                if (refund == null) {
                    ctx.redirect("/admin?error=noOrderFound#returnAdmin");
                    return;
                }

                Order order = orderMapper.getById(refund.getOrderId());
                if (order == null) {
                    ctx.redirect("/admin?error=noOrderFound#returnAdmin");
                    return;
                }

                double total = order.getItems().stream()
                        .mapToDouble(i -> i.getPrice() * i.getQuantity())
                        .sum();

                userMapper.addBalance(refund.getUserId(), total);
                refundMapper.updateRefundStatus(returnId, "Confirmed");
                ctx.redirect("/admin?error=returnConfirmed#returnAdmin");
                return;

            } else if ("reject".equals(action)) {

                refundMapper.updateRefundStatus(returnId, "Rejected");
                ctx.redirect("/admin?error=returnRejected#returnAdmin");
                return;

            }

        } catch (Exception e) {
            ctx.redirect("/admin?error=dbError#returnAdmin");
        }
    }

    // ______________________________________________________

    public void returnOrderShowAll(Context ctx) throws SQLException {
        List<Refund> refunds = refundMapper.getAllRefunds();

        ctx.json(refunds);
    }

}