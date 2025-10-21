// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.entities.Order;
import dk.cupcake.entities.OrderItem;
import dk.cupcake.entities.User;
import dk.cupcake.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    // Attributes
    private final OrderItemMapper orderItemMapper = new OrderItemMapper();
    private final UserMapper userMapper = new UserMapper();

    // _________________________________________________________
    // Get order by id (not user_id).

    public Order getById(int id) throws SQLException {

        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return toOrder(rs);
            return null;

        }

    }

    // _________________________________________________________
    // Get all orders

    public List<Order> getAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(toOrder(rs));
            }
        }
        return orders;
    }

    // _________________________________________________________
    // Get all orders for a specific user

    public List<Order> getByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(toOrder(rs));
            }
        }
        return orders;
    }

    // _________________________________________________________
    // Create a new order

    public Order newOrder(int userID) throws SQLException {
        String sql = "INSERT INTO orders (user_id) VALUES (?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userID);

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return new Order(keys.getInt(1), userID);
                //order.setId(keys.getInt(1));
            }

        }
        return null;
    }

    // _________________________________________________________
    // Delete

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________

    private Order toOrder(ResultSet rs) throws SQLException {

        Order order = new Order();

        order.setId(rs.getInt("id"));
        order.setStatus(rs.getString("status"));
        order.setCreatedAt(rs.getTimestamp("created_at"));

        int userId = rs.getInt("user_id");
        User user = userMapper.getById(userId);
        order.setUser(user);

        int deliveryId = rs.getInt("delivery_method_id");
        if (!rs.wasNull()) {
            order.setDeliveryMethodId(deliveryId);
        }

        int paymentId = rs.getInt("payment_method_id");
        if (!rs.wasNull()) {
            order.setPaymentMethodId(paymentId);
        }

        String address = rs.getString("delivery_address");
        if (address != null) {
            order.setDeliveryAddress(address);
        }

        List<OrderItem> items = orderItemMapper.getOrderByID(order.getId());
        order.setItems(new ArrayList<>(items));

        return order;
    }

    // _________________________________________________________

    public void updateMethodsAndAddress(int orderId, Integer deliveryMethodId, Integer paymentMethodId, String deliveryAddress) throws SQLException {
        String sql = "UPDATE orders SET delivery_method_id = ?, payment_method_id = ?, delivery_address = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (deliveryMethodId != null) {
                stmt.setInt(1, deliveryMethodId);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            if (paymentMethodId != null) {
                stmt.setInt(2, paymentMethodId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setString(3, deliveryAddress);
            stmt.setInt(4, orderId);
            stmt.executeUpdate();

        }

    }

    // _________________________________________________________

    public void updateOrderStatus(Order order) throws SQLException {

        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getStatus());
            stmt.setInt(2, order.getId());
            stmt.executeUpdate();

        }

    }

    // _________________________________________________________

    public Order getUserOrderByID(int id, int user_id) throws SQLException {

        String sql = "SELECT * FROM orders WHERE id = ? AND user_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setInt(2, user_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return toOrder(rs);

            return null;

        }

    }

    // _________________________________________________________

    public Order getById(int id, int user_id) throws SQLException {

        String sql = "SELECT * FROM orders WHERE id = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setInt(2, user_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return toOrder(rs);
            return null;

        }

    }
} // OrderMapper end