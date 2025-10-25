// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.CupcakeTopping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CupcakeToppingMapperTest {

    // Attributes
    private static CupcakeToppingMapper toppingMapper;

    // _________________________________________________________

    @BeforeAll
    static void beforeAll() throws SQLException {
        Database.setDatabaseName("Cupcake_test");
    }

    // _________________________________________________________

    @BeforeEach
    void setUp() throws SQLException {
        toppingMapper = new CupcakeToppingMapper();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE cupcake_toppings RESTART IDENTITY CASCADE");
        }
    }

    // _________________________________________________________

    @Test
    void shouldCreateAndRetrieveTopping() throws SQLException {

        // Arrange
        CupcakeTopping topping = new CupcakeTopping();
        topping.setName("Chocolate");
        topping.setPrice(5.0);

        // Act
        toppingMapper.newTopping(topping);
        CupcakeTopping retrieved = toppingMapper.getById(topping.getId());

        // Assert
        assertTrue(topping.getId() > 0);
        assertNotNull(retrieved);
        assertEquals("Chocolate", retrieved.getName());
        assertEquals(5.0, retrieved.getPrice());

    }

    // _________________________________________________________

    @Test
    void shouldUpdateTopping() throws SQLException {

        // Arrange
        CupcakeTopping topping = new CupcakeTopping();
        topping.setName("Sprinkles");
        topping.setPrice(3.0);
        toppingMapper.newTopping(topping);
        topping.setName("Rainbow Sprinkles");
        topping.setPrice(3.5);

        // Act
        toppingMapper.update(topping);
        CupcakeTopping updated = toppingMapper.getById(topping.getId());

        // Assert
        assertNotNull(updated);
        assertEquals("Rainbow Sprinkles", updated.getName());
        assertEquals(3.5, updated.getPrice());

    }

    // _________________________________________________________

    @Test
    void shouldDeleteTopping() throws SQLException {

        // Arrange
        CupcakeTopping topping = new CupcakeTopping();
        topping.setName("Caramel");
        topping.setPrice(4.0);
        toppingMapper.newTopping(topping);

        // Act
        toppingMapper.delete(topping.getId());
        CupcakeTopping deleted = toppingMapper.getById(topping.getId());

        // Assert
        assertNull(deleted);

    }

    // _________________________________________________________

    @Test
    void shouldRetrieveAllToppings() throws SQLException {

        // Arrange
        CupcakeTopping t1 = new CupcakeTopping();
        t1.setName("Choco");
        t1.setPrice(5.0);
        toppingMapper.newTopping(t1);
        CupcakeTopping t2 = new CupcakeTopping();
        t2.setName("Vanilla");
        t2.setPrice(4.5);
        toppingMapper.newTopping(t2);

        // Act
        List<CupcakeTopping> allToppings = toppingMapper.getAll();

        // Assert
        assertEquals(2, allToppings.size());

    }

} // CupcakeToppingMapperTest end