// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.entities.Cart;
import dk.cupcake.entities.CartItem;
import dk.cupcake.entities.CupcakeFlavor;
import dk.cupcake.entities.CupcakeTopping;
import dk.cupcake.entities.Product;
import dk.cupcake.mapper.ProductMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CartController {

    private final ProductMapper productMapper = new ProductMapper();

    public static void registerRoutes(Javalin app) {
        CartController controller = new CartController();
        app.post("/api/cart/items", controller::addItem);
        app.get("/api/cart", controller::getCart);
    }

    private Cart getSessionCart(Context ctx) {
        Cart cart = ctx.sessionAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            ctx.sessionAttribute("cart", cart);
        }
        return cart;
    }

    public void addItem(Context ctx) {
        try {
            String productIdStr = ctx.formParam("productId");
            String flavorIdStr = ctx.formParam("flavorId");
            String toppingIdStr = ctx.formParam("toppingId");
            String quantityStr = ctx.formParam("quantity");

            int quantity = quantityStr != null ? Integer.parseInt(quantityStr) : 1;
            if (quantity < 1) quantity = 1;

            CartItem item = new CartItem();
            item.setQuantity(quantity);

            if (productIdStr != null) {
                int productId = Integer.parseInt(productIdStr);
                Product p = productMapper.getById(productId);
                if (p == null) {
                    ctx.status(404).result("NEJ ikke fundet");
                    return;
                }
                item.setProductId(productId);
                item.setName(p.getName());
                item.setUnitPrice(p.getPrice());
            } else if (flavorIdStr != null && toppingIdStr != null) {
                int flavorId = Integer.parseInt(flavorIdStr);
                int toppingId = Integer.parseInt(toppingIdStr);

                String sql = "SELECT f.name AS fname, f.price AS fprice, t.name AS tname, t.price AS tprice FROM cupcake_flavor f, cupcake_toppings t WHERE f.id = ? AND t.id = ?";
                var conn = dk.cupcake.db.Database.getConnection();
                try (var ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, flavorId);
                    ps.setInt(2, toppingId);
                    var rs = ps.executeQuery();
                    if (!rs.next()) {
                        ctx.status(404).result("Kombination ikke fundet");
                        return;
                    }
                    String fname = rs.getString("fname");
                    double fprice = rs.getDouble("fprice");
                    String tname = rs.getString("tname");
                    double tprice = rs.getDouble("tprice");

                    item.setFlavorId(flavorId);
                    item.setToppingId(toppingId);
                    item.setName(fname + " + " + tname);
                    item.setUnitPrice(fprice + tprice);
                }
            } else {
                ctx.status(400).result("Ugyldige parametre");
                return;
            }

            Cart cart = getSessionCart(ctx);
            cart.addItem(item);

            Map<String, Object> payload = new HashMap<>();
            payload.put("itemCount", cart.getItemCount());
            payload.put("total", cart.getTotal());
            payload.put("items", cart.getItems().stream().map(ci -> Map.of(
                    "name", ci.getName(),
                    "qty", ci.getQuantity(),
                    "unitPrice", ci.getUnitPrice(),
                    "lineTotal", ci.getLineTotal()
            )).toArray());

            ctx.json(payload);
        } catch (SQLException e) {
            ctx.status(500).result("Database fejl");
        } catch (Exception e) {
            ctx.status(400).result("Fejl");
        }
    }

    public void getCart(Context ctx) {
        Cart cart = getSessionCart(ctx);
        Map<String, Object> payload = new HashMap<>();
        payload.put("itemCount", cart.getItemCount());
        payload.put("total", cart.getTotal());
        payload.put("items", cart.getItems().stream().map(ci -> Map.of(
                "name", ci.getName(),
                "qty", ci.getQuantity(),
                "unitPrice", ci.getUnitPrice(),
                "lineTotal", ci.getLineTotal()
        )).toArray());
        ctx.json(payload);
    }
}


