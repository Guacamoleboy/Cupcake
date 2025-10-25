/*

    Database.java | Sat op så Unit Test kan køres fra ekstern database
    navngivet Cupcake_test (Se Unit Test).

    Fungerer som normalt, men med forbedret implementering så unit tests kan køres.

    Sidst opdateret af: Guacamoleboy
    Dato: 25/10-2025

*/

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
