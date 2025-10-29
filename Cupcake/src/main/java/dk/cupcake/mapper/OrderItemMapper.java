// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.entities.OrderItem;
import dk.cupcake.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemMapper {

    // _________________________________________________________

    public OrderItem getById(int id) throws SQLException {
        String sql = "SELECT * FROM order_items WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return toOrderItem(rs);
            return null;
        }
    }

    // _________________________________________________________

    public List<OrderItem> getAll() throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(toOrderItem(rs));
            }
        }
        return items;
    }

    // _________________________________________________________

    private OrderItem toOrderItem(ResultSet rs) throws SQLException {

        int productId = rs.getInt("product_id");
        String title = rs.getString("name");
        String description = rs.getString("description");
        double price = rs.getDouble("price");
        int quantity = rs.getInt("quantity");
        int top = rs.getInt("topping_id");
        int buttom = rs.getInt("bottom_id");

        OrderItem item = new OrderItem(productId, title, description, price, quantity, top, buttom);

        item.setId(rs.getInt("id"));

        return item;
    }

    // _________________________________________________________

    public static void addOrderItem(int orderID, OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, topping_id, bottom_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, orderID);
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setInt(4, item.getToppingId());
            stmt.setInt(5, item.getBottomId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }
        }
    }


    // _________________________________________________________

    public static void updateQuantity(int orderID, int id, int quantity) throws SQLException {
        String sql = "UPDATE order_items SET quantity = ? WHERE order_id = ? AND id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, orderID);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________

    public static void deleteOrderItem(int orderID, int productID) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ? AND product_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            stmt.setInt(2, productID);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________

    public static void deleteAmount(int orderID, int productID, int amount) throws SQLException {
        String sql = "UPDATE order_items SET quantity = quantity - ? WHERE order_id = ? AND product_id = ? RETURNING quantity";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, amount);
            stmt.setInt(2, orderID);
            stmt.setInt(3, productID);

            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                int newQuantity = rs.getInt("quantity");
                if (newQuantity <= 0) {
                    deleteOrderItem(orderID, productID);
                }
            }
        }
    }

    // _________________________________________________________

    public static void emptyBasket(int orderID) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________

    public static ArrayList<OrderItem> getOrderByID(int orderID) throws SQLException {
        ArrayList<OrderItem> orderItems = new ArrayList<>();

        String sql = "SELECT * FROM order_items " +
                "JOIN products ON order_items.product_id = products.id " +
                "WHERE order_items.order_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String title = rs.getString("name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    int top = rs.getInt("topping_id");
                    int buttom = rs.getInt("bottom_id");

                    orderItems.add(new OrderItem(productId, title, description, price, quantity, top, buttom));
                }
            }
        }

        return orderItems;
    }


} // OrderItemMapper end