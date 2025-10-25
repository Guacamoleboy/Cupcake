// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionMapperTest {

    // Attributes
    private TransactionMapper transactionMapper;

    // _________________________________________________________

    @BeforeAll
    static void beforeAll() throws SQLException {
        Database.setDatabaseName("Cupcake_test");
    }

    // _________________________________________________________

    @BeforeEach
    void setUp() throws SQLException {
        transactionMapper = new TransactionMapper();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("TRUNCATE TABLE transactions RESTART IDENTITY CASCADE");
             stmt.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
        }
    }

    // _________________________________________________________

    @Test
    void shouldCreateAndRetrieveTransaction() throws SQLException {

        // Arrange
        int userId;
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("INSERT INTO users (username, password_hash, email) VALUES ('user1', 'pw1', 'user1@test.com')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rs = stmt.getGeneratedKeys();
             rs.next();
             userId = rs.getInt(1);
        }
        Transaction t = new Transaction();
        t.setUserId(userId);
        t.setAmount(100.0);
        t.setDescription("Test transaction");

        // Act
        transactionMapper.newTransaction(t);
        Transaction retrieved = transactionMapper.getById(t.getId());

        // Assert
        assertTrue(t.getId() > 0);
        assertNotNull(retrieved);
        assertEquals(userId, retrieved.getUserId());
        assertEquals(100.0, retrieved.getAmount());
        assertEquals("Test transaction", retrieved.getDescription());

    }

    // _________________________________________________________

    @Test
    void shouldUpdateTransaction() throws SQLException {

        // Arrange
        int userId;
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("INSERT INTO users (username, password_hash, email) VALUES ('user1', 'pw1', 'user1@test.com')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rs = stmt.getGeneratedKeys();
             rs.next();
             userId = rs.getInt(1);
        }
        Transaction t = new Transaction();
        t.setUserId(userId);
        t.setAmount(50.0);
        t.setDescription("Initial transaction");
        transactionMapper.newTransaction(t);
        t.setAmount(75.0);
        t.setDescription("Updated transaction");

        // Act
        transactionMapper.update(t);
        Transaction updated = transactionMapper.getById(t.getId());

        // Assert
        assertNotNull(updated);
        assertEquals(75.0, updated.getAmount());
        assertEquals("Updated transaction", updated.getDescription());

    }

    // _________________________________________________________

    @Test
    void shouldDeleteTransaction() throws SQLException {

        // Arrange
        int userId;
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("INSERT INTO users (username, password_hash, email) VALUES ('user1', 'pw1', 'user1@test.com')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rs = stmt.getGeneratedKeys();
             rs.next();
             userId = rs.getInt(1);
        }
        Transaction t = new Transaction();
        t.setUserId(userId);
        t.setAmount(20.0);
        t.setDescription("To be deleted");
        transactionMapper.newTransaction(t);

        // Act
        transactionMapper.delete(t.getId());
        Transaction deleted = transactionMapper.getById(t.getId());

        // Assert
        assertNull(deleted);

    }

    // _________________________________________________________

    @Test
    void shouldRetrieveAllTransactions() throws SQLException {

        // Arrange
        int user1Id, user2Id;
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("INSERT INTO users (username, password_hash, email) VALUES ('user1', 'pw1', 'user1@test.com')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rs1 = stmt.getGeneratedKeys(); rs1.next(); user1Id = rs1.getInt(1);
             stmt.execute("INSERT INTO users (username, password_hash, email) VALUES ('user2', 'pw2', 'user2@test.com')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rs2 = stmt.getGeneratedKeys(); rs2.next(); user2Id = rs2.getInt(1);
        }
        Transaction t1 = new Transaction(); t1.setUserId(user1Id); t1.setAmount(10.0); t1.setDescription("T1"); transactionMapper.newTransaction(t1);
        Transaction t2 = new Transaction(); t2.setUserId(user2Id); t2.setAmount(20.0); t2.setDescription("T2"); transactionMapper.newTransaction(t2);

        // Act
        List<Transaction> allTransactions = transactionMapper.getAll();

        // Assert
        assertEquals(2, allTransactions.size());

    }

    // _________________________________________________________

    @Test
    void shouldRetrieveTransactionsByUserId() throws SQLException {

        // Arrange
        int user1Id;
        int user2Id;
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("INSERT INTO users (username, password_hash, email) VALUES ('user1', 'pw1', 'user1@test.com')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rs1 = stmt.getGeneratedKeys();
             rs1.next();
             user1Id = rs1.getInt(1);
             stmt.execute("INSERT INTO users (username, password_hash, email) VALUES ('user2', 'pw2', 'user2@test.com')", Statement.RETURN_GENERATED_KEYS);
             ResultSet rs2 = stmt.getGeneratedKeys();
             rs2.next();
             user2Id = rs2.getInt(1);
        }
        Transaction t1 = new Transaction(); t1.setUserId(user1Id); t1.setAmount(10.0); t1.setDescription("User1 T1"); transactionMapper.newTransaction(t1);
        Transaction t2 = new Transaction(); t2.setUserId(user1Id); t2.setAmount(20.0); t2.setDescription("User1 T2"); transactionMapper.newTransaction(t2);
        Transaction t3 = new Transaction(); t3.setUserId(user2Id); t3.setAmount(30.0); t3.setDescription("User2 T1"); transactionMapper.newTransaction(t3);

        // Act
        List<Transaction> user1Transactions = transactionMapper.getByUserId(user1Id);

        // Assert
        assertEquals(2, user1Transactions.size());
        for (Transaction t : user1Transactions) {
            assertEquals(user1Id, t.getUserId());
        }

    }

} // TransactionMapperTest end