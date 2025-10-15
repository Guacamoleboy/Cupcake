// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.entities.Product;
import dk.cupcake.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static class ComboResult {
        private final String name;
        private final double price;

        public ComboResult(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
    }

    // _________________________________________________________

    public Product getById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return toProduct(rs);
            return null;
        }
    }

    // _________________________________________________________
    // Gets all products

    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(toProduct(rs));
            }
        }
        return products;
    }

    // _________________________________________________________

    public List<Product> getByCategoryId(int categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(toProduct(rs));
            }
        }
        return products;
    }

    // _________________________________________________________

    public void newProduct(Product p) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, image_url, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setDouble(3, p.getPrice());
            stmt.setString(4, p.getImageUrl());
            stmt.setInt(5, p.getCategoryId());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                p.setId(keys.getInt(1));
            }
        }
    }

    // _________________________________________________________

    public void update(Product p) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, image_url = ?, category_id = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setDouble(3, p.getPrice());
            stmt.setString(4, p.getImageUrl());
            stmt.setInt(5, p.getCategoryId());
            stmt.setInt(6, p.getId());

            stmt.executeUpdate();
        }
    }

    // _________________________________________________________

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________

    private Product toProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setFlavor_id(rs.getInt("flavor_id"));
        p.setTopping_id(rs.getInt("topping_id"));
        return p;
    }

    // _________________________________________________________

    public List<Product> getByTopping(String topping) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.* FROM products p " +
                "JOIN cupcake_toppings t ON t.id = p.topping_id " +
                "WHERE LOWER(t.name) = LOWER(?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, topping);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(toProduct(rs));
            }
        }
        return products;
    }

    // _________________________________________________________

    public List<Product> getByBund(String bund) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.* FROM products p " +
                "JOIN cupcake_flavor f ON f.id = p.flavor_id " +
                "WHERE LOWER(f.name) = LOWER(?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bund);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(toProduct(rs));
            }
        }
        return products;
    }

    // _________________________________________________________

    public List<Product> searchByName(String name) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(toProduct(rs));
            }
        }
        return products;
    }

    // _________________________________________________________

    public List<Product> getByToppingAndBund(String topping, String bund) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.* FROM products p " +
                "JOIN cupcake_toppings t ON t.id = p.topping_id " +
                "JOIN cupcake_flavor f ON f.id = p.flavor_id " +
                "WHERE t.name = ? AND f.name = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, topping);
            stmt.setString(2, bund);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(toProduct(rs));
            }
        }
        return products;
    }

    // _________________________________________________________

    public ComboResult getCupcakeCombo(int flavorId, int toppingId) throws SQLException {
        String sql = "SELECT f.name AS fname, f.price AS fprice, t.name AS tname, t.price AS tprice " +
                "FROM cupcake_flavor f, cupcake_toppings t WHERE f.id = ? AND t.id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, flavorId);
            ps.setInt(2, toppingId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            String fname = rs.getString("fname");
            double fprice = rs.getDouble("fprice");
            String tname = rs.getString("tname");
            double tprice = rs.getDouble("tprice");
            return new ComboResult(fname + " + " + tname, fprice + tprice);
        }
    }

} // ProductMapper end