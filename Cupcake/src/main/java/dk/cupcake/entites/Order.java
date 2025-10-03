// Package
package dk.cupcake.entites;

// Imports
import java.sql.Timestamp;
import java.util.*;

public class Order {

    // Attributes
    private int id;
    private User user;
    private String status;
    private Timestamp createdAt;
    private ArrayList<OrderItem> items;

    // ___________________________________________________

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    // ___________________________________________________

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
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

    public User getUser() {
        return user;
    }

    // ___________________________________________________

    public void setUser(User user) {
        this.user = user;
    }

    // ___________________________________________________

    public int getId() {
        return id;
    }

    // ___________________________________________________

    public void setId(int id) {
        this.id = id;
    }

    // ___________________________________________________

    public String getStatus() {
        return status;
    }

    // ___________________________________________________

    public void setStatus(String status) {
        this.status = status;
    }

} // Order end
