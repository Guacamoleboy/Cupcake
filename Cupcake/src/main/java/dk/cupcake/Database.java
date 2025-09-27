// Package
package dk.cupcake;

// Imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // Attributes
    private static final String URL = "jdbc:postgresql://localhost:5433/cupcake"; // Jeg bruger 33. Måske jeres skal i 32.
    private static final String USER = "postgres";
    private static final String PASSWORD = "dinmor";

    // ________________________________________________

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

} // Database end