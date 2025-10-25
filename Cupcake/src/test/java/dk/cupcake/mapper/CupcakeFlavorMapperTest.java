// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.CupcakeFlavor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CupcakeFlavorMapperTest {

    // Attributes
    private CupcakeFlavorMapper flavorMapper;

    // _______________________________________________________________

    @BeforeAll
    static void beforeAll() {
        Database.setDatabaseName("Cupcake_test");
    }

    // _______________________________________________________________

    @BeforeEach
    void setUp() throws SQLException {
        flavorMapper = new CupcakeFlavorMapper();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE cupcake_flavor RESTART IDENTITY CASCADE");
        }
    }

    // _______________________________________________________________

    @Test
    void shouldCreateAndRetrieveFlavor() throws SQLException {

        // Arrange
        CupcakeFlavor flavor = new CupcakeFlavor();
        flavor.setName("Choco Test");
        flavor.setPrice(15.0);

        // Act
        flavorMapper.newFlavor(flavor);
        CupcakeFlavor retrieved = flavorMapper.getById(flavor.getId());

        // Assert
        assertTrue(flavor.getId() > 0);
        assertNotNull(retrieved);
        assertEquals("Choco Test", retrieved.getName());
        assertEquals(15.0, retrieved.getPrice());

    }

    // _______________________________________________________________

    @Test
    void shouldUpdateFlavor() throws SQLException {

        // Arrange
        CupcakeFlavor flavor = new CupcakeFlavor();
        flavor.setName("Vanilla Test");
        flavor.setPrice(12.5);
        flavorMapper.newFlavor(flavor);

        // Act
        flavor.setName("Vanilla Updated");
        flavor.setPrice(13.5);
        flavorMapper.update(flavor);
        CupcakeFlavor updated = flavorMapper.getById(flavor.getId());

        // Assert
        assertEquals("Vanilla Updated", updated.getName());
        assertEquals(13.5, updated.getPrice());

    }

    // _______________________________________________________________

    @Test
    void shouldDeleteFlavor() throws SQLException {

        // Arrange
        CupcakeFlavor flavor = new CupcakeFlavor();
        flavor.setName("Delete Test");
        flavor.setPrice(10.0);
        flavorMapper.newFlavor(flavor);

        // Act
        flavorMapper.delete(flavor.getId());
        CupcakeFlavor deleted = flavorMapper.getById(flavor.getId());

        // Assert
        assertNull(deleted);

    }

    // _______________________________________________________________

    @Test
    void shouldRetrieveAllFlavors() throws SQLException {

        // Arrange
        CupcakeFlavor f1 = new CupcakeFlavor(); f1.setName("F1"); f1.setPrice(10); flavorMapper.newFlavor(f1);
        CupcakeFlavor f2 = new CupcakeFlavor(); f2.setName("F2"); f2.setPrice(12); flavorMapper.newFlavor(f2);

        // Act
        List<CupcakeFlavor> allFlavors = flavorMapper.getAll();

        // Assert
        assertEquals(2, allFlavors.size());
        assertTrue(allFlavors.stream().anyMatch(f -> f.getName().equals("F1")));
        assertTrue(allFlavors.stream().anyMatch(f -> f.getName().equals("F2")));

    }

} // CupcakeFlavorMapperTest end