// Package
package dk.cupcake.entities;

// Imports
import java.sql.Timestamp;

public class Transaction {

    // Attributes
    private int id;
    private int userId;
    private double amount;
    private Timestamp createdAt;
    private String description;

    // ___________________________________________________

    public int getUserId() {
        return userId;
    }

    // ___________________________________________________

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    // ___________________________________________________

    public void setDescription(String description) {
        this.description = description;
    }

    // ___________________________________________________

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // ___________________________________________________

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // ___________________________________________________

    public double getAmount() {
        return amount;
    }

    // ___________________________________________________

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // ___________________________________________________

    public int getId() {
        return id;
    }

    // ___________________________________________________

    public void setId(int id) {
        this.id = id;
    }

} // Transaction end
