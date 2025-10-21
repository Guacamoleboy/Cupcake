package dk.cupcake.controller;

import dk.cupcake.entities.Order;
import dk.cupcake.entities.Product;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.ProductMapper;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductController {

    // Attributes
    private final ProductMapper productMapper = new ProductMapper();

    // ____________________________________________

    public static void registerRoutes(Javalin app) {

        ProductController controller = new ProductController();

        app.get("/order", controller::showAll);
        app.get("/orderContinue", controller::showAllContinue);
        app.get("/order/category/{id}", controller::showByCategory);
        app.get("/order/topping/{name}", controller::showByTopping);
        app.get("/order/bund/{name}", controller::showByBund);
        app.get("/order/search", controller::searchByTitle);
        app.get("/custom/preview", controller::previewCupcake);

    }

    // ____________________________________________

    public void showAll(Context ctx) {
        try {
            List<Product> products = productMapper.getAll();
            ctx.html(ThymeleafSetup.render("order.html", Map.of("products", products)));
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Database fejl: " + e.getMessage());
        }
    }

    // ____________________________________________

    public void showAllContinue(Context ctx) throws SQLException {
        String orderIdParam = ctx.queryParam("orderId");
        OrderMapper orderMapper = new OrderMapper();
        Order order = orderMapper.getById(Integer.parseInt(orderIdParam));
        ctx.sessionAttribute("order", order);
        try {
            List<Product> products = productMapper.getAll();
            ctx.html(ThymeleafSetup.render("order.html", Map.of("products", products)));
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Database fejl: " + e.getMessage());
        }
    }

    // ____________________________________________

    public void showByCategory(Context ctx) {
        try {
            int categoryId = Integer.parseInt(ctx.pathParam("id"));
            List<Product> products = productMapper.getByCategoryId(categoryId);
            ctx.html(ThymeleafSetup.render("order.html", Map.of("products", products)));
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Database fejl: " + e.getMessage());
        }
    }

    // ____________________________________________

    public void showByTopping(Context ctx) {
        try {
            String topping = ctx.pathParam("name");
            List<Product> products = productMapper.getByTopping(topping);
            ctx.html(ThymeleafSetup.render("order.html", Map.of("products", products)));
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Database fejl: " + e.getMessage());
        }
    }

    // ____________________________________________

    public void showByBund(Context ctx) {
        try {
            String bund = ctx.pathParam("name");
            List<Product> products = productMapper.getByBund(bund);
            ctx.html(ThymeleafSetup.render("order.html", Map.of("products", products)));
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Database fejl: " + e.getMessage());
        }
    }

    // ____________________________________________

    public void searchByTitle(Context ctx) {
        try {
            String query = ctx.queryParam("q"); // ?q=searchTerm
            if (query == null || query.isEmpty()) {
                query = ""; // return all if empty
            }
            List<Product> products = productMapper.searchByName(query);
            ctx.html(ThymeleafSetup.render("order.html", Map.of("products", products)));
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Database fejl: " + e.getMessage());
        }
    }

    // ____________________________________________

    public void previewCupcake(Context ctx) {
        try {
            String topping = ctx.queryParam("topping");
            String bund = ctx.queryParam("bund");

            if (topping == null || bund == null) {
                ctx.status(400).result("Topping og bund skal vælges");
                return;
            }

            List<Product> products = productMapper.getByToppingAndBund(topping, bund);

            if (!products.isEmpty()) {
                Product p = products.get(0); // take the first match
                ctx.json(Map.of(
                        "id", p.getId(),
                        "name", p.getName(),
                        "imageUrl", p.getImageUrl(),
                        "price", p.getPrice()
                ));
            } else {
                ctx.status(404).result("Ingen cupcake fundet");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Database fejl: " + e.getMessage());
        }
    }

} // ProductController end