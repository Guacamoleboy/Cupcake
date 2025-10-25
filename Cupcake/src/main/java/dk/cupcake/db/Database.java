// Package
package dk.cupcake.db;

// Imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // Attributes
    private static String databaseName = "Cupcake";
    private static final String URL = "jdbc:postgresql://localhost:5433/%s";
    private static final String USER = "postgres";
    private static final String PASSWORD = "dinmor";

    // ________________________________________________

    public static Connection getConnection() throws SQLException {
        String url = String.format(URL, databaseName);
        return DriverManager.getConnection(url, USER, PASSWORD);
    }

    // ________________________________________________

    public static void setDatabaseName(String newDatabaseName) {
        databaseName = newDatabaseName;
    }

} // Database end
