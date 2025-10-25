// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OrderMapperTest {

    // Attributes
    private OrderMapper orderMapper;
    private int userId;
    private int deliveryMethodId;
    private int paymentMethodId;

    // _________________________________________________________

    @BeforeAll
    static void beforeAll() throws SQLException {
        Database.setDatabaseName("Cupcake_test");
    }

    // _________________________________________________________

    @BeforeEach
    void setUp() throws SQLException {
        orderMapper = new OrderMapper();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("TRUNCATE TABLE orders RESTART IDENTITY CASCADE");
             stmt.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
             stmt.execute("TRUNCATE TABLE payment_methods RESTART IDENTITY CASCADE");
             stmt.execute("TRUNCATE TABLE delivery_methods RESTART IDENTITY CASCADE");

             stmt.execute("INSERT INTO users (username, password_hash, email) VALUES ('user1', 'pw1', 'user1@test.com')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rsUser = stmt.getGeneratedKeys();
             rsUser.next();
             userId = rsUser.getInt(1);

             stmt.execute("INSERT INTO delivery_methods (name) VALUES ('Standard')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rsDelivery = stmt.getGeneratedKeys();
             rsDelivery.next();
             deliveryMethodId = rsDelivery.getInt(1);

             stmt.execute("INSERT INTO payment_methods (name) VALUES ('Card')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rsPayment = stmt.getGeneratedKeys();
             rsPayment.next();
             paymentMethodId = rsPayment.getInt(1);

        }
    }

    // _________________________________________________________

    @Test
    void shouldCreateAndRetrieveOrder() throws SQLException {

        // Arrange
        Order order = orderMapper.newOrder(userId);

        // Act
        Order retrieved = orderMapper.getById(order.getId());

        // Assert
        assertNotNull(order);
        assertTrue(order.getId() > 0);
        assertEquals(userId, order.getUser().getId());
        assertNotNull(retrieved);
        assertEquals(order.getId(), retrieved.getId());
        assertEquals(userId, retrieved.getUser().getId());

    }

    // _________________________________________________________

    @Test
    void shouldGetAllOrders() throws SQLException {

        // Arrange
        orderMapper.newOrder(userId);
        orderMapper.newOrder(userId);

        // Act
        List<Order> orders = orderMapper.getAll();

        // Assert
        assertEquals(2, orders.size());

    }

    // _________________________________________________________

    @Test
    void shouldGetOrdersByUserId() throws SQLException {

        // Arrange
        orderMapper.newOrder(userId);
        orderMapper.newOrder(userId);

        // Act
        List<Order> orders = orderMapper.getByUserId(userId);

        // Assert
        assertEquals(2, orders.size());
        for (Order o : orders) {
            assertEquals(userId, o.getUser().getId());
        }

    }

    // _________________________________________________________

    @Test
    void shouldUpdateOrderStatusAndMethods() throws SQLException {

        // Arrange
        Order order = orderMapper.newOrder(userId);
        order.setStatus("SHIPPED");

        // Act
        orderMapper.updateOrderStatus(order);
        orderMapper.updateMethodsAndAddress(order.getId(), deliveryMethodId, paymentMethodId, "123 Cupcake St.");
        Order updated = orderMapper.getById(order.getId());

        // Assert
        assertEquals("SHIPPED", updated.getStatus());
        assertEquals(deliveryMethodId, updated.getDeliveryMethodId());
        assertEquals(paymentMethodId, updated.getPaymentMethodId());
        assertEquals("123 Cupcake St.", updated.getDeliveryAddress());
    }

    // _________________________________________________________

    @Test
    void shouldDeleteOrder() throws SQLException {

        // Arrange
        Order order = orderMapper.newOrder(userId);

        // Act
        orderMapper.delete(order.getId());
        Order deleted = orderMapper.getById(order.getId());

        // Assert
        assertNull(deleted);

    }

} // OrderMapperTest end