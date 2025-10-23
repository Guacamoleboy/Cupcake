package dk.cupcake.mapper;

import dk.cupcake.db.Database;
import dk.cupcake.entities.Order;
import dk.cupcake.entities.Refund;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RefundMapper {

    public Refund createRefund(Order order, String reason) throws SQLException {

        String sql = "INSERT INTO refunds (order_id, user_id, reason, created_at, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getId());
            ps.setInt(2, order.getUser().getId());
            ps.setString(3, reason);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setString(5, "open");

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {

                    return new Refund(rs.getInt(1), order.getId(), order.getUser().getId(), reason, rs.getTimestamp("created_at"), "active");

                }
            }
        }

        return null;
    }

    public List<Refund> getAllRefunds(int userId) throws SQLException {
        String sql = "SELECT * FROM refunds WHERE user_id = ? AND status IN ('open', 'closed') ORDER BY created_at DESC";
        return getRefunds(sql, userId);
    }

    // Til admin
    public List<Refund> getAllRefunds() throws SQLException {
        String sql = "SELECT * FROM refunds WHERE status IN ('open', 'closed') ORDER BY created_at DESC";
        return getRefunds(sql, null);
    }


    public List<Refund> getAllActiveRefunds(int userId) throws SQLException {
        String sql = "SELECT * FROM refunds WHERE user_id = ? AND status = 'open' ORDER BY created_at DESC";
        return getRefunds(sql, userId);
    }

    // Til admin
    public List<Refund> getAllActiveRefunds() throws SQLException {
        String sql = "SELECT * FROM refunds WHERE status = 'open' ORDER BY created_at DESC";
        return getRefunds(sql, null);
    }

    public List<Refund> getAllClosedRefunds(int userId) throws SQLException {
        String sql = "SELECT * FROM refunds WHERE user_id = ? AND status = 'closed' ORDER BY created_at DESC";
        return getRefunds(sql, userId);
    }

    // Til admmin
    public List<Refund> getAllClosedRefunds() throws SQLException {
        String sql = "SELECT * FROM refunds WHERE status = 'closed' ORDER BY created_at DESC";
        return getRefunds(sql, null);
    }


    private List<Refund> getRefunds(String sql, Integer userId) throws SQLException {
        List<Refund> refunds = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (userId != null) ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    refunds.add(mapRefund(rs));
                }
            }
        }

        return refunds;
    }

    private Refund mapRefund(ResultSet rs) throws SQLException {

        return new Refund(
                rs.getInt("id"),
                rs.getInt("order_id"),
                rs.getInt("user_id"),
                rs.getString("reason"),
                rs.getTimestamp("created_at"),
                rs.getString("status")
        );

    }
}
