package dk.cupcake.mapper;

import dk.cupcake.db.Database;
import dk.cupcake.entities.Coupon;
import java.sql.*;
import java.time.Instant;

public class CouponMapper {

    public Coupon getCouponByCode(String code) throws SQLException {
        String sql = "SELECT * FROM coupons WHERE code = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp now = Timestamp.from(Instant.now());
                Timestamp validFrom = rs.getTimestamp("valid_from");
                Timestamp validUntil = rs.getTimestamp("valid_until");

                if (now.after(validFrom) && (validUntil == null || now.before(validUntil))) {
                    return new Coupon(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getInt("discount_percent"),
                            validFrom,
                            validUntil
                    );
                }
            }
        }
        return null;
    }
}
