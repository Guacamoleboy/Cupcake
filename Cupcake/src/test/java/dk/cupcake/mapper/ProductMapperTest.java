// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperTest {

    // Attributes
    private ProductMapper productMapper;

    // _________________________________________________________

    @BeforeAll
    static void beforeAll() {
        Database.setDatabaseName("Cupcake_test");
    }

    // _________________________________________________________

    @BeforeEach
    void setUp() throws SQLException {

        productMapper = new ProductMapper();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("TRUNCATE TABLE products, categories, cupcake_flavor, cupcake_toppings RESTART IDENTITY CASCADE");
             stmt.execute("INSERT INTO categories (name) VALUES ('Test Category')");
             stmt.execute("INSERT INTO cupcake_flavor (name, price) VALUES ('Test Flavor', 5.0)");
             stmt.execute("INSERT INTO cupcake_toppings (name, price) VALUES ('Test Topping', 2.5)");
             stmt.execute("INSERT INTO products (name, description, price, image_url, category_id, flavor_id, topping_id) " +
             "VALUES ('Test Product', 'Beskrivelse', 10.0, 'url', 1, 1, 1)");
        }
    }


    // _________________________________________________________

    @Test
    void shouldInsertAndRetrieveProduct() throws SQLException {

        // Arrange
        Product p = new Product();
        p.setName("Test Cupcake");
        p.setDescription("Delicious test cupcake");
        p.setPrice(25.0);
        p.setCategoryId(1);
        p.setFlavor_id(1);
        p.setTopping_id(1);

        // Act
        productMapper.newProduct(p);
        Product retrieved = productMapper.getById(p.getId());

        // Assert
        assertNotNull(retrieved);
        assertEquals("Test Cupcake", retrieved.getName());
        assertEquals(25.0, retrieved.getPrice());
        assertEquals(1, retrieved.getCategoryId());

    }

    // _________________________________________________________

    @Test
    void shouldUpdateProduct() throws SQLException {

        // Arrange
        Product p = new Product();
        p.setName("Original Cupcake");
        p.setPrice(20.0);
        p.setCategoryId(1);
        p.setFlavor_id(1);
        p.setTopping_id(1);
        productMapper.newProduct(p);

        // Act
        p.setName("Updated Cupcake");
        p.setPrice(30.0);
        productMapper.update(p);
        Product updated = productMapper.getById(p.getId());

        // Assert
        assertEquals("Updated Cupcake", updated.getName());
        assertEquals(30.0, updated.getPrice());

    }

    // _________________________________________________________

    @Test
    void shouldDeleteProduct() throws SQLException {

        // Arrange
        Product p = new Product();
        p.setName("Cupcake to delete");
        p.setCategoryId(1);
        p.setFlavor_id(1);
        p.setTopping_id(1);
        productMapper.newProduct(p);

        // Act
        productMapper.delete(p.getId());
        Product deleted = productMapper.getById(p.getId());

        // Assert
        assertNull(deleted);

    }

    // _________________________________________________________

    @Test
    void shouldRetrieveAllProducts() throws SQLException {

        // Arrange
        Product p1 = new Product();
        p1.setName("A");
        p1.setPrice(10.0);
        p1.setCategoryId(1);
        p1.setFlavor_id(1);
        p1.setTopping_id(1);
        productMapper.newProduct(p1);

        Product p2 = new Product();
        p2.setName("B");
        p2.setPrice(12.0);
        p2.setCategoryId(1);
        p2.setFlavor_id(1);
        p2.setTopping_id(1);
        productMapper.newProduct(p2);

        // Act
        List<Product> allProducts = productMapper.getAll();

        // Assert
        assertEquals(3, allProducts.size());

    }

} // ProductMapperTest end