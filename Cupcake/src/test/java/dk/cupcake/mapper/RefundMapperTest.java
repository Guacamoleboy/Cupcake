// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.Order;
import dk.cupcake.entities.Refund;
import dk.cupcake.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RefundMapperTest {

    // Attributes
    private RefundMapper refundMapper;
    private User testUser;
    private Order testOrder;

    // ___________________________________________________________________

    @BeforeAll
    static void beforeAll() throws SQLException {
        Database.setDatabaseName("Cupcake_test");
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("INSERT INTO delivery_methods (name, price) VALUES ('Test Delivery', 10.0) ON CONFLICT (name) DO NOTHING");
             stmt.execute("INSERT INTO payment_methods (name) VALUES ('Test Payment') ON CONFLICT (name) DO NOTHING");
        }
    }

    // ___________________________________________________________________

    @BeforeEach
    void setUp() throws SQLException {
        refundMapper = new RefundMapper();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

             stmt.execute("TRUNCATE TABLE refunds, orders, users RESTART IDENTITY CASCADE");
             stmt.execute("INSERT INTO users (email, password_hash, role, username, phone, payment_attached, balance) " +
             "VALUES ('refund@test.dk', 'pass', 'customer', 'refunduser', '12345678', false, 0)");
             stmt.execute("INSERT INTO orders (user_id, status, delivery_method_id, payment_method_id, delivery_address) " +
             "VALUES (1, 'open', 1, 1, 'Test Address')");
        }

        testUser = new User();
        testUser.setId(1);
        testOrder = new Order();
        testOrder.setId(1);
        testOrder.setUser(testUser);
    }

    // ___________________________________________________________________

    @Test
    void shouldCreateRefund() throws SQLException {

        // Arange

        // Act
        Refund refund = refundMapper.createRefund(testOrder, "Reason for refund");

        // Assert
        assertNotNull(refund);
        assertEquals(testOrder.getId(), refund.getOrderId());
        assertEquals(testUser.getId(), refund.getUserId());
        assertEquals("Reason for refund", refund.getReason());
        assertEquals("open", refund.getStatus());

    }

    // ___________________________________________________________________

    @Test
    void shouldRetrieveRefundsByUser() throws SQLException {

        // Arange

        // Act
        refundMapper.createRefund(testOrder, "First");
        refundMapper.createRefund(testOrder, "Second");
        List<Refund> refunds = refundMapper.getAllRefunds(testUser.getId());

        // Assert
        assertEquals(2, refunds.size());

    }

    // ___________________________________________________________________

    @Test
    void shouldUpdateRefundStatus() throws SQLException {

        // Arange

        // Act
        Refund refund = refundMapper.createRefund(testOrder, "Need update");
        refundMapper.updateRefundStatus(refund.getId(), "closed");
        Refund updated = refundMapper.getRefundByID(refund.getId());

        // Assert
        assertEquals("closed", updated.getStatus());

    }

} // RefundMapperTest