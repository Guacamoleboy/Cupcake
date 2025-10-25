// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryMapperTest {

    // Attributes
    private CategoryMapper categoryMapper;

    // ____________________________________________________

    @BeforeAll
    static void beforeAll() {
        Database.setDatabaseName("Cupcake_test");
    }

    // ____________________________________________________

    @BeforeEach
    void setUp() throws SQLException {

        categoryMapper = new CategoryMapper();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE categories RESTART IDENTITY CASCADE");
        }
    }


    // ____________________________________________________

    @Test
    void shouldCreateAndRetrieveCategory() throws SQLException {

        // Arange
        Category c = new Category();
        c.setName("New Category");

        // Act
        categoryMapper.newCategory(c);
        Category retrieved = categoryMapper.getById(c.getId());

        // Assert
        assertTrue(c.getId() > 0);
        assertNotNull(retrieved);
        assertEquals("New Category", retrieved.getName());

    }

    // ____________________________________________________

    @Test
    void shouldUpdateCategory() throws SQLException {

        // Arrange
        Category c = new Category();
        c.setName("Old Name");
        categoryMapper.newCategory(c);

        // Act
        c.setName("Updated Name");
        categoryMapper.update(c);
        Category updated = categoryMapper.getById(c.getId());

        // Assert
        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());

    }

    // ____________________________________________________

    @Test
    void shouldDeleteCategory() throws SQLException {

        // Arrange
        Category c = new Category();
        c.setName("To Be Deleted");
        categoryMapper.newCategory(c);

        // Act
        categoryMapper.delete(c.getId());
        Category deleted = categoryMapper.getById(c.getId());

        // Assert
        assertNull(deleted);

    }

    // ____________________________________________________

    @Test
    void shouldRetrieveAllCategories() throws SQLException {

        // Arrange
        categoryMapper.newCategory(new Category() {{ setName("Cat1"); }});
        categoryMapper.newCategory(new Category() {{ setName("Cat2"); }});

        // Act
        List<Category> categories = categoryMapper.getAll();

        // Assert
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Cat1")));
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Cat2")));

    }

} // CategoryMapperTest end