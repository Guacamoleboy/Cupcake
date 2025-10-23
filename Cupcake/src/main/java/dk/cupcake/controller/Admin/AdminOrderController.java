package dk.cupcake.controller.Admin;

import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.RefundMapper;
import dk.cupcake.mapper.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminOrderController {

    private static final UserMapper userMapper = new UserMapper();
    private static final OrderMapper orderMapper = new OrderMapper();
    private static final RefundMapper refundMapper = new RefundMapper();

    // ______________________________________________________

    public static void registerRoutes(Javalin app) {

        AdminOrderController controller = new AdminOrderController();

        app.post("/admin/searchOrder", controller::searchOrder);
        app.post("/admin/manageOrder", controller::manageOrder);
        app.post("/admin/searchOrders", controller::searchOrders);
        app.post("/admin/createOrder", controller::createOrder);
        app.post("/admin/manageRefusion", controller::manageRefusion);
        app.post("/admin/returnOrder", controller::returnOrder);

    }

    // ______________________________________________________

    public void searchOrder(Context ctx) {

    }

    // ______________________________________________________

    public void manageOrder(Context ctx) {

    }

    // ______________________________________________________

    public void searchOrders(Context ctx) {

    }

    // ______________________________________________________

    public void createOrder(Context ctx) {

    }

    // ______________________________________________________

    public void manageRefusion(Context ctx) {

    }

    // ______________________________________________________

    public void returnOrder(Context ctx) {

    }

}
