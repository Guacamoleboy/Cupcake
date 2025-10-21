package dk.cupcake.controller.Profile;

import dk.cupcake.entities.Order;
import dk.cupcake.entities.Refund;
import dk.cupcake.entities.User;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.RefundMapper;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ProfileReturnController {

    private static final OrderMapper orderMapper = new OrderMapper();
    private static final RefundMapper refundMapper = new RefundMapper();

    // ______________________________________________________________

    public static void registerRoutes(Javalin app) {

        ProfileReturnController controller = new ProfileReturnController();

        app.post("/createRefund", controller::createRefund);
        app.post("/getActiveRefunds", controller::getActiveRefunds);
        app.post("/getClosedRefunds", controller::getClosedRefunds);

    }

    // ______________________________________________________________

    public void createRefund(Context ctx) throws SQLException {

        User user = ctx.sessionAttribute("user");
        if (user == null) {

            ctx.status(401).json(Map.of("error", "Ikke logget ind"));
            return;

        }

        String orderIdParam = ctx.formParam("orderId");
        String reason = ctx.formParam("reason");

        if (orderIdParam == null || reason == null || reason.isEmpty()) {

            ctx.status(400).json(Map.of("error", "Manglende ordreID eller grundlag"));
            return;

        }

        int orderID;

        try {

            orderID = Integer.parseInt(orderIdParam);

        } catch (NumberFormatException e) {

            ctx.status(400).json(Map.of("error", "Ugyldigt ordreID"));
            return;

        }


        Order order = orderMapper.getById(orderID, user.getId());
        if (order == null) {

            ctx.status(404).json(Map.of("error", "Ordre ikke fundet"));
            return;

        }

        Refund refund = refundMapper.createRefund(order, reason);
        if (refund == null) {

            ctx.status(500).json(Map.of("error", "Kunne ikke oprette refund"));
            return;

        }

        ctx.json(Map.of(
                "refundId", refund.getId(),
                "orderId", refund.getOrderId(),
                "reason", refund.getReason(),
                "createdAt", refund.getCreatedAt().toString()
        ));

    }

    // ______________________________________________________________

    public void getActiveRefunds(Context ctx) throws SQLException {

        User user = ctx.sessionAttribute("user");
        if (user == null) {

            ctx.status(401).json(Map.of("error", "Ikke logget ind"));
            return;

        }

        List<Refund> refunds = refundMapper.getAllActiveRefunds(user.getId());

        ctx.json(refunds);
    }

    // ______________________________________________________________

    public void getClosedRefunds(Context ctx) throws SQLException {

        User user = ctx.sessionAttribute("user");
        if (user == null) {

            ctx.status(401).json(Map.of("error", "Ikke logget ind"));
            return;

        }

        List<Refund> refunds = refundMapper.getAllClosedRefunds(user.getId());

        ctx.json(refunds);
    }

}
