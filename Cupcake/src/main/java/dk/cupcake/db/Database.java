// Package
package dk.cupcake.db;

// Imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // Attributes
    private static final String URL = "jdbc:postgresql://localhost:5432/Beyondborders";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    // ________________________________________________

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

} // Database end
