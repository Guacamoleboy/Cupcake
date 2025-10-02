// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.Order;
import dk.cupcake.OrderItem;
import dk.cupcake.User;
import dk.cupcake.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    // Attributes
    private final OrderItemMapper orderItemMapper = new OrderItemMapper();
    private final UserMapper userMapper = new UserMapper();

    // _________________________________________________________
    // Get order by id

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

    public void newOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, status) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getUser().getId());
            stmt.setString(2, order.getStatus());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                order.setId(keys.getInt(1));
            }

            // Insert order items if any
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    item.setOrderId(order.getId());
                    orderItemMapper.newOrderItem(item);
                }
            }
        }
    }

    // _________________________________________________________
    // Update an order

    public void update(Order order) throws SQLException {
        String sql = "UPDATE orders SET user_id = ?, status = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getUser().getId());
            stmt.setString(2, order.getStatus());
            stmt.setInt(3, order.getId());

            stmt.executeUpdate();

            // Update order items if any
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    if (item.getId() > 0) {
                        orderItemMapper.update(item);
                    } else {
                        item.setOrderId(order.getId());
                        orderItemMapper.newOrderItem(item);
                    }
                }
            }
        }
    }

    // _________________________________________________________
    // Delete an order (cascades to order items)

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Epstein

    private Order toOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setStatus(rs.getString("status"));
        order.setCreatedAt(rs.getTimestamp("created_at"));

        // Set user
        User user = userMapper.getById(rs.getInt("user_id"));
        order.setUser(user);

        // Set items
        List<OrderItem> items = orderItemMapper.getByOrderId(order.getId());
        order.setItems(new ArrayList<>(items));

        return order;
    }

} // OrderMapper end