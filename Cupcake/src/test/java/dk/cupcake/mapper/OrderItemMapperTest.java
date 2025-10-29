// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.OrderItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class OrderItemMapperTest {

    // Attributes
    private int orderID = 1;
    private int productID1;
    private int productID2;
    private int toppingID;
    private int bottomID;

    // _____________________________________________________________

    @BeforeAll
    static void beforeAll() throws SQLException {
        Database.setDatabaseName("Cupcake_test");
    }

    // _____________________________________________________________

    @BeforeEach
    void setUp() throws SQLException {

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("TRUNCATE TABLE order_items RESTART IDENTITY CASCADE");
             stmt.execute("TRUNCATE TABLE products RESTART IDENTITY CASCADE");
             stmt.execute("TRUNCATE TABLE orders RESTART IDENTITY CASCADE");
             stmt.execute("TRUNCATE TABLE cupcake_flavor RESTART IDENTITY CASCADE");
             stmt.execute("TRUNCATE TABLE cupcake_toppings RESTART IDENTITY CASCADE");
             stmt.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");

             stmt.execute("INSERT INTO users (username, password_hash, email) VALUES ('user1', 'pw1', 'user1@test.com')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rsUser = stmt.getGeneratedKeys();
             rsUser.next();
             int userID = rsUser.getInt(1);

             stmt.execute("INSERT INTO cupcake_toppings (name, price) VALUES ('Choco', 5.0)", Statement.RETURN_GENERATED_KEYS);
             ResultSet rsTopping = stmt.getGeneratedKeys();
             rsTopping.next();
             int toppingID = rsTopping.getInt(1);

             stmt.execute("INSERT INTO cupcake_flavor (name, price) VALUES ('Vanilla', 10.0)", Statement.RETURN_GENERATED_KEYS);
             ResultSet rsBottom = stmt.getGeneratedKeys();
             rsBottom.next();
             int bottomID = rsBottom.getInt(1);

             stmt.execute("INSERT INTO orders (user_id, status) VALUES (1, 'PENDING')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rsOrder = stmt.getGeneratedKeys();
             rsOrder.next();
             orderID = rsOrder.getInt(1);

             stmt.execute("INSERT INTO products (name, description, price) VALUES ('Cupcake1', 'Delicious1', 25.0)", Statement.RETURN_GENERATED_KEYS);
             ResultSet rs1 = stmt.getGeneratedKeys();
             rs1.next();
             productID1 = rs1.getInt(1);

             stmt.execute("INSERT INTO products (name, description, price) VALUES ('Cupcake2', 'Delicious2', 30.0)", Statement.RETURN_GENERATED_KEYS);
             ResultSet rs2 = stmt.getGeneratedKeys();
             rs2.next();
             productID2 = rs2.getInt(1);

             this.toppingID = toppingID;
             this.bottomID = bottomID;

        }

    }

    // _____________________________________________________________

    @Test
    void shouldAddAndRetrieveOrderItem() throws SQLException {

        // Arrange
        OrderItem item = new OrderItem(productID1, "Cupcake1", "Delicious1", 25.0, 2, toppingID, bottomID);

        // Act
        OrderItemMapper.addOrderItem(orderID, item);
        ArrayList<OrderItem> items = OrderItemMapper.getOrderByID(orderID);
        OrderItem retrieved = items.get(0);

        // Assert
        assertEquals(1, items.size());
        assertEquals(productID1, retrieved.getProductId());
        assertEquals(2, retrieved.getQuantity());
        assertEquals(toppingID, retrieved.getToppingId());
        assertEquals(bottomID, retrieved.getBottomId());

    }

    // _____________________________________________________________

    @Test
    void shouldUpdateQuantity() throws SQLException {

        // Arrange
        OrderItem item = new OrderItem(productID1, "Cupcake1", "Delicious1", 25.0, 2, toppingID, bottomID);

        // Act
        OrderItemMapper.addOrderItem(orderID, item);
        OrderItemMapper.updateQuantity(orderID, item.getId(), 5);
        ArrayList<OrderItem> items = OrderItemMapper.getOrderByID(orderID);

        // Assert
        assertEquals(1, items.size());
        assertEquals(5, items.get(0).getQuantity());

    }

    // _____________________________________________________________

    @Test
    void shouldDeleteOrderItem() throws SQLException {

        // Arrange
        OrderItem item = new OrderItem(productID1, "Cupcake1", "Delicious1", 25.0, 3, toppingID, bottomID);

        // Act
        OrderItemMapper.addOrderItem(orderID, item);
        OrderItemMapper.deleteOrderItem(orderID, productID1);
        ArrayList<OrderItem> items = OrderItemMapper.getOrderByID(orderID);

        // Assert
        assertTrue(items.isEmpty());

    }

    // _____________________________________________________________

    @Test
    void shouldDeleteAmountAndRemoveIfZero() throws SQLException {

        // Arrange
        OrderItem item = new OrderItem(productID1, "Cupcake1", "Delicious1", 25.0, 3, toppingID, bottomID);

        // Act
        OrderItemMapper.addOrderItem(orderID, item);
        OrderItemMapper.deleteAmount(orderID, productID1, 2);
        ArrayList<OrderItem> itemsAfterReduce = OrderItemMapper.getOrderByID(orderID);
        OrderItemMapper.deleteAmount(orderID, productID1, 1);
        ArrayList<OrderItem> itemsAfterRemove = OrderItemMapper.getOrderByID(orderID);

        // Assert
        assertEquals(1, itemsAfterReduce.size());
        assertEquals(1, itemsAfterReduce.get(0).getQuantity());
        assertTrue(itemsAfterRemove.isEmpty());

    }

    // _____________________________________________________________

    @Test
    void shouldEmptyBasket() throws SQLException {

        // Arrange
        OrderItem item1 = new OrderItem(productID1, "Cupcake1", "Delicious1", 25.0, 2, toppingID, bottomID);
        OrderItem item2 = new OrderItem(productID2, "Cupcake2", "Delicious2", 30.0, 1, toppingID, bottomID);

        // Act
        OrderItemMapper.addOrderItem(orderID, item1);
        OrderItemMapper.addOrderItem(orderID, item2);
        OrderItemMapper.emptyBasket(orderID);
        ArrayList<OrderItem> items = OrderItemMapper.getOrderByID(orderID);

        // Assert
        assertTrue(items.isEmpty());

    }

} // OrderItemMapperTest end