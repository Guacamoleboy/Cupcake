// Package
package dk.cupcake;

// Imports
import dk.cupcake.server.Server;
import java.sql.SQLException;


public class Main {

    // Attributes

    // _____________________________________________________

    public static void main(String[] args) throws SQLException {

        Server server = new Server();
        server.start(7000);
    }

} // Main end