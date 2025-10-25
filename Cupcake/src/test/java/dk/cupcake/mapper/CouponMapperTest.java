// Package
package dk.cupcake.mapper;

// Imports
import static org.junit.jupiter.api.Assertions.*;
import dk.cupcake.db.Database;
import dk.cupcake.entities.Coupon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CouponMapperTest {

    // Attributes
    private CouponMapper couponMapper;

    // ________________________________________________________

    @BeforeAll
    static void beforeAll() {
        Database.setDatabaseName("Cupcake_test");
    }

    // ________________________________________________________

    @BeforeEach
    void setUp() throws SQLException {

        couponMapper = new CouponMapper();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
             stmt.execute("TRUNCATE TABLE coupons RESTART IDENTITY CASCADE");
        }

    }

    // ________________________________________________________

    @Test
    void shouldRetrieveValidCoupon() throws SQLException {

        // Arange
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO coupons (code, discount_percent, valid_from, valid_until) VALUES (?, ?, ?, ?)")) {

            Timestamp now = Timestamp.from(Instant.now());
            Timestamp tomorrow = Timestamp.from(Instant.now().plus(1, ChronoUnit.DAYS));

            stmt.setString(1, "TEST10");
            stmt.setInt(2, 10);
            stmt.setTimestamp(3, now);
            stmt.setTimestamp(4, tomorrow);
            stmt.executeUpdate();
        }

        // Act
        Coupon c = couponMapper.getCouponByCode("TEST10");

        // Assert
        assertNotNull(c);
        assertEquals("TEST10", c.getCode());
        assertEquals(10, c.getDiscountPercent());

    }

    // ________________________________________________________

    @Test
    void shouldReturnNullForNonExistingCoupon() throws SQLException {

        // Arange

        // Act
        Coupon c = couponMapper.getCouponByCode("FAILTEST");

        // Assert
        assertNull(c);

    }

    // ________________________________________________________

    @Test
    void shouldReturnNullForExpiredOrFutureCoupon() throws SQLException {

        // Arange
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
         "INSERT INTO coupons (code, discount_percent, valid_from, valid_until) VALUES (?, ?, ?, ?)")) {

            // Expired
            Timestamp pastStart = Timestamp.from(Instant.now().minus(2, ChronoUnit.DAYS));
            Timestamp pastEnd = Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS));
            stmt.setString(1, "OLD10");
            stmt.setInt(2, 10);
            stmt.setTimestamp(3, pastStart);
            stmt.setTimestamp(4, pastEnd);
            stmt.executeUpdate();

            // Non-active
            Timestamp futureStart = Timestamp.from(Instant.now().plus(1, ChronoUnit.DAYS));
            Timestamp futureEnd = Timestamp.from(Instant.now().plus(2, ChronoUnit.DAYS));
            stmt.setString(1, "FUTURE10");
            stmt.setInt(2, 15);
            stmt.setTimestamp(3, futureStart);
            stmt.setTimestamp(4, futureEnd);
            stmt.executeUpdate();
        }

        // Act
        Coupon expiredCoupon = couponMapper.getCouponByCode("OLD10");
        Coupon futureCoupon = couponMapper.getCouponByCode("FUTURE10");

        // Assert
        assertNull(expiredCoupon);
        assertNull(futureCoupon);

    }

} // CouponMapperTest end