// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.PaymentMethods;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodsMapper {

    // Attributes

    // __________________________________________________________

    public PaymentMethods getById(int id) throws SQLException {
        String sql = "SELECT * FROM payment_methods WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setInt(1, id);
             ResultSet rs = stmt.executeQuery();

             if (rs.next()) return toPaymentMethod(rs);
             return null;
        }
    }

    // __________________________________________________________

    public List<PaymentMethods> getAll() throws SQLException {
        List<PaymentMethods> methods = new ArrayList<>();
        String sql = "SELECT * FROM payment_methods";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

             while (rs.next()) {
                 methods.add(toPaymentMethod(rs));
            }
        }
        return methods;
    }

    // __________________________________________________________

    private PaymentMethods toPaymentMethod(ResultSet rs) throws SQLException {
        return new PaymentMethods(
                rs.getInt("id"),
                rs.getString("name")
        );
    }

} // PaymentMethodsMapper end