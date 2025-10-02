// Package
package dk.cupcake;

// Imports
import java.sql.Timestamp;

public class User {

    // Attributes
    private int id;
    private String email;
    private String password; // This is hashed
    private String role;
    private String username;
    private Timestamp createdAt;

    // ___________________________________________________

    public int getId() {
        return id;
    }

    // ___________________________________________________

    public void setId(int id) {
        this.id = id;
    }

    // ___________________________________________________

    public String getEmail() {
        return email;
    }

    // ___________________________________________________

    public void setEmail(String email) {
        this.email = email;
    }

    // ___________________________________________________

    public String getPassword() {
        return password;
    }

    // ___________________________________________________

    public void setPassword(String password) {
        this.password = password;
    }

    // ___________________________________________________

    public String getRole() {
        return role;
    }

    // ___________________________________________________

    public void setRole(String role) {
        this.role = role;
    }

    // ___________________________________________________

    public String getUsername() {
        return username;
    }

    // ___________________________________________________

    public void setUsername(String username) {
        this.username = username;
    }

    // ___________________________________________________

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // ___________________________________________________

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

} // User end