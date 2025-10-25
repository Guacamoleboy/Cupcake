// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.entities.CupcakeTopping;
import dk.cupcake.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CupcakeToppingMapper {

    // Attributes

    // _________________________________________________________

    public CupcakeTopping getById(int id) throws SQLException {
        String sql = "SELECT * FROM cupcake_toppings WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return toCupcakeTopping(rs);
            return null;
        }
    }

    // _________________________________________________________

    public List<CupcakeTopping> getAll() throws SQLException {
        List<CupcakeTopping> toppings = new ArrayList<>();
        String sql = "SELECT * FROM cupcake_toppings";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                toppings.add(toCupcakeTopping(rs));
            }
        }
        return toppings;
    }

    // _________________________________________________________

    public void newTopping(CupcakeTopping topping) throws SQLException {
        String sql = "INSERT INTO cupcake_toppings (name, price) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, topping.getName());
            stmt.setDouble(2, topping.getPrice());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                topping.setId(keys.getInt(1));
            }
        }
    }

    // _________________________________________________________

    public void update(CupcakeTopping topping) throws SQLException {
        String sql = "UPDATE cupcake_toppings SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, topping.getName());
            stmt.setDouble(2, topping.getPrice());
            stmt.setInt(3, topping.getId());
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM cupcake_toppings WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________

    private CupcakeTopping toCupcakeTopping(ResultSet rs) throws SQLException {
        CupcakeTopping topping = new CupcakeTopping();
        topping.setId(rs.getInt("id"));
        topping.setName(rs.getString("name"));
        topping.setPrice(rs.getDouble("price"));
        return topping;
    }

} // CupcakeToppingMapper end