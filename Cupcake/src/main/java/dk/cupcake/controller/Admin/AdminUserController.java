package dk.cupcake.controller.Admin;

import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.RefundMapper;
import dk.cupcake.mapper.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminUserController {

    private static final UserMapper userMapper = new UserMapper();
    private static final OrderMapper orderMapper = new OrderMapper();
    private static final RefundMapper refundMapper = new RefundMapper();

    // ______________________________________________________

    public static void registerRoutes(Javalin app) {

        AdminUserController controller = new AdminUserController();

        app.post("/admin/searchUser", controller::searchUser);
        app.post("/admin/addUser", controller::addUser);
        app.post("/admin/removeUser", controller::removeUser);

    }

    // ______________________________________________________

    public void searchUser(Context ctx) {

    }

    // ______________________________________________________

    public void addUser(Context ctx) {

    }

    // ______________________________________________________

    public void removeUser(Context ctx) {

    }

}
