// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.DeliveryMethods;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryMethodsMapper {

    // Attributes

    // ___________________________________________________________

    public DeliveryMethods getById(int id) throws SQLException {
        String sql = "SELECT * FROM delivery_methods WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setInt(1, id);
             ResultSet rs = stmt.executeQuery();
             if (rs.next()) return toDeliveryMethod(rs);
             return null;
        }
    }

    // ___________________________________________________________

    public List<DeliveryMethods> getAll() throws SQLException {
        List<DeliveryMethods> methods = new ArrayList<>();
        String sql = "SELECT * FROM delivery_methods";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             while (rs.next()) {
                 methods.add(toDeliveryMethod(rs));
             }
        }
        return methods;
    }

    // ___________________________________________________________

    private DeliveryMethods toDeliveryMethod(ResultSet rs) throws SQLException {
        return new DeliveryMethods(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price")
        );
    }

} // DeliveryMethodsMapper end