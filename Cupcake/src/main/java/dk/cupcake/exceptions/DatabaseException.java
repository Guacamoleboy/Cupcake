package dk.cupcake.exceptions;

public class DatabaseException extends Exception {

    // Attributes

    // __________________________________________________________

    public DatabaseException(String userMessage) {
        super(userMessage);
        System.out.println("userMessage: " + userMessage);
    }

    // __________________________________________________________

    public DatabaseException(String userMessage, String systemMessage) {
        super(userMessage);
        System.out.println("userMessage: " + userMessage);
        System.out.println("errorMessage: " + systemMessage);
    }

} // DatabaseException End