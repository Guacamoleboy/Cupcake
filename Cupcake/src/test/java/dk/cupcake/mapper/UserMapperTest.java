// Package
package dk.cupcake.mapper;

// Imports
import static org.junit.jupiter.api.Assertions.*;
import dk.cupcake.db.Database;
import dk.cupcake.entities.User;
import dk.cupcake.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class UserMapperTest {

    // Attributes
    private UserMapper usermapper;

    // ______________________________________________________________

    @BeforeAll
    static void beforeAll() {
        Database.setDatabaseName("Cupcake_test");
    }

    // ______________________________________________________________

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {

        usermapper = new UserMapper();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
        }

    }

    // ______________________________________________________________

    @Test
    void shouldInsertAndRetrieveUser() throws SQLException, DatabaseException {

        // Arange
        User u = new User();
        u.setEmail("testuser@test.dk");
        u.setPasswordHash("guacamoleboy123!");
        u.setRole("customer");
        u.setUsername("unittest");
        u.setPhone("50502211");
        u.setPaymentAttached(false);
        u.setBalance(0.0);

        // Act
        usermapper.newUser(u);
        User unitTestOne = usermapper.getById(u.getId());

        // Assert
        assertNotNull(unitTestOne);
        assertTrue(u.getId() > 0);
        assertEquals("testuser@test.dk", unitTestOne.getEmail());
        assertEquals("unittest", unitTestOne.getUsername());
        assertEquals("customer", unitTestOne.getRole());
        assertFalse(unitTestOne.isPaymentAttached());
        assertEquals(0.0, unitTestOne.getBalance());

    }

    // ______________________________________________________________

    @Test
    void shouldCheckIfUserExists() throws SQLException, DatabaseException {

        // Arrange
        User u = new User();
        u.setEmail("realuser@test.dk");
        u.setPasswordHash("pass123");
        u.setRole("customer");
        u.setUsername("realuser");
        u.setPhone("12345678");
        u.setPaymentAttached(false);
        u.setBalance(0.0);
        usermapper.newUser(u);

        // Act & Assert
        assertTrue(usermapper.existsByEmailOrUsername("realuser@test.dk", "doesnotmatter")); // Only Email
        assertTrue(usermapper.existsByEmailOrUsername("doesnotmatter", "realuser"));         // Only Username
        assertTrue(usermapper.existsByEmailOrUsername("realuser@test.dk", "realuser"));      // Both
        assertFalse(usermapper.existsByEmailOrUsername("notfound@test.dk", "notfound"));     // None

    }

    // ______________________________________________________________

    @Test
    void shouldUpdateUser() throws SQLException, DatabaseException {

        // Arange
        User u = new User();
        u.setEmail("old@test.dk");
        u.setPasswordHash("pass");
        u.setRole("customer");
        u.setUsername("olduser");
        u.setPhone("11111111");
        u.setPaymentAttached(false);
        u.setBalance(0.0);

        // Act
        usermapper.newUser(u);
        u.setEmail("new@test.dk");
        u.setUsername("newuser");
        u.setBalance(50.0);
        usermapper.update(u);
        User updated = usermapper.getById(u.getId());

        // Assert
        assertEquals("new@test.dk", updated.getEmail());
        assertEquals("newuser", updated.getUsername());
        assertEquals(50.0, updated.getBalance());

    }

    // ______________________________________________________________

    @Test
    void shouldDeleteUser() throws SQLException, DatabaseException {

        // Arange
        User u = new User();
        u.setEmail("tobedeleted@test.dk");
        u.setPasswordHash("pass");
        u.setRole("customer");
        u.setUsername("deleteuser");
        u.setPhone("22222222");
        u.setPaymentAttached(false);
        u.setBalance(0.0);

        // Act
        usermapper.newUser(u);
        usermapper.delete(u.getId());

        // Assert
        assertNull(usermapper.getById(u.getId()));

    }

    // ______________________________________________________________

    @Test
    void shouldAddBalance() throws SQLException, DatabaseException {

        // Arange
        User u = new User();
        u.setEmail("balance@test.dk");
        u.setPasswordHash("pass");
        u.setRole("customer");
        u.setUsername("balanceuser");
        u.setPhone("33333333");
        u.setPaymentAttached(false);
        u.setBalance(100.0);

        // Act
        usermapper.newUser(u);
        usermapper.addBalance(u.getId(), 50.0);
        usermapper.addBalance("balanceuser", 25.0);
        User updated = usermapper.getById(u.getId());

        // Assert
        assertEquals(175.0, updated.getBalance());

    }

    // ______________________________________________________________

    @Test
    void shouldLoginUser() throws SQLException, DatabaseException {

        // Arrange
        User u = new User();
        u.setEmail("login@test.dk");
        u.setPasswordHash("pass123");
        u.setRole("customer");
        u.setUsername("loginuser");
        u.setPhone("44444444");
        u.setPaymentAttached(false);
        u.setBalance(0.0);

        // Act
        usermapper.newUser(u);

        // Assert
        // Login
        User loggedIn = usermapper.login("loginuser", "pass123");
        assertNotNull(loggedIn);
        System.out.println("Bruger logget ind korrekt");

        // Wrong Login
        try {
            usermapper.login("loginuser", "wrongpass");
            fail("Expected DatabaseException to be thrown");
        } catch (DatabaseException e) {
            assertTrue(e.getMessage().contains("Forkert brugernavn"));
        }

    }

} // UserMapperTest end