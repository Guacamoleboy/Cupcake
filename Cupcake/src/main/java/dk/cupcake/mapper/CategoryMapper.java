// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.entities.Category;
import dk.cupcake.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    // Attributes

    // _________________________________________________________
    // Get a category by id

    public Category getById(int id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return toCategory(rs);
            return null;
        }
    }

    // _________________________________________________________
    // Get all categories

    public List<Category> getAll() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(toCategory(rs));
            }
        }
        return categories;
    }

    // _________________________________________________________
    // Create a new category

    public void newCategory(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getName());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                category.setId(keys.getInt(1));
            }
        }
    }

    // _________________________________________________________
    // Update a category

    public void update(Category category) throws SQLException {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Delete a category

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Trump

    private Category toCategory(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        return c;
    }

} // CategoryMapper end