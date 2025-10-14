// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.entities.CupcakeFlavor;
import dk.cupcake.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CupcakeFlavorMapper {

    // Attributes

    // _________________________________________________________
    // Get a cupcake flavor by id

    public CupcakeFlavor getById(int id) throws SQLException {
        String sql = "SELECT * FROM cupcake_flavor WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return toCupcakeFlavor(rs);
            return null;
        }
    }

    // _________________________________________________________
    // Get all cupcake flavors

    public List<CupcakeFlavor> getAll() throws SQLException {
        List<CupcakeFlavor> flavors = new ArrayList<>();
        String sql = "SELECT * FROM cupcake_flavor";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                flavors.add(toCupcakeFlavor(rs));
            }
        }
        return flavors;
    }

    // _________________________________________________________
    // Create a new cupcake flavor

    public void newFlavor(CupcakeFlavor flavor) throws SQLException {
        String sql = "INSERT INTO cupcake_flavor (name, price) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, flavor.getName());
            stmt.setDouble(2, flavor.getPrice());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                flavor.setId(keys.getInt(1));
            }
        }
    }

    // _________________________________________________________
    // Update a cupcake flavor

    public void update(CupcakeFlavor flavor) throws SQLException {
        String sql = "UPDATE cupcake_flavor SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, flavor.getName());
            stmt.setDouble(2, flavor.getPrice());
            stmt.setInt(3, flavor.getId());

            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Delete a cupcake flavor

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM cupcake_flavor WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // J.Cole

    private CupcakeFlavor toCupcakeFlavor(ResultSet rs) throws SQLException {
        CupcakeFlavor flavor = new CupcakeFlavor();
        flavor.setId(rs.getInt("id"));
        flavor.setName(rs.getString("name"));
        flavor.setPrice(rs.getDouble("price"));
        return flavor;
    }

} // CupcakeFlavorMapper end