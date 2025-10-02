// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.OrderItem;
import dk.cupcake.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemMapper {

    // _________________________________________________________
    // Gets an order item by id

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
    // Gets all order items

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
    // Gets all items for a specific order

    public List<OrderItem> getByOrderId(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(toOrderItem(rs));
            }
        }
        return items;
    }

    // _________________________________________________________
    // Creates a new order item

    public void newOrderItem(OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, bottom_id, topping_id, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, item.getOrderId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getBottomId());
            stmt.setInt(4, item.getToppingId());
            stmt.setInt(5, item.getQuantity());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                item.setId(keys.getInt(1));
            }
        }
    }

    // _________________________________________________________
    // Updates an existing order item

    public void update(OrderItem item) throws SQLException {
        String sql = "UPDATE order_items SET order_id = ?, product_id = ?, bottom_id = ?, topping_id = ?, quantity = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getOrderId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getBottomId());
            stmt.setInt(4, item.getToppingId());
            stmt.setInt(5, item.getQuantity());
            stmt.setInt(6, item.getId());

            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Deletes an order item

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM order_items WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Diddy

    private OrderItem toOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setBottomId(rs.getInt("bottom_id"));
        item.setToppingId(rs.getInt("topping_id"));
        item.setQuantity(rs.getInt("quantity"));
        return item;
    }

} // OrderItemMapper end