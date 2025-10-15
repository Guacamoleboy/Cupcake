// Package
package dk.cupcake.entities;

// Imports
import java.sql.Timestamp;

public class Product {

    // Attributes
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int categoryId;
    private int topping_id;
    private int flavor_id;
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

    public String getName() {
        return name;
    }

    // ___________________________________________________

    public void setName(String name) {
        this.name = name;
    }

    // ___________________________________________________

    public String getDescription() {
        return description;
    }

    // ___________________________________________________

    public void setDescription(String description) {
        this.description = description;
    }

    // ___________________________________________________

    public double getPrice() {
        return price;
    }

    // ___________________________________________________

    public void setPrice(double price) {
        this.price = price;
    }

    // ___________________________________________________

    public String getImageUrl() {
        return imageUrl;
    }

    // ___________________________________________________

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // ___________________________________________________

    public int getCategoryId() {
        return categoryId;
    }

    // ___________________________________________________

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public int getTopping_id() {
        return topping_id;
    }

    // ___________________________________________________

    public void setTopping_id(int topping_id) {
        this.topping_id = topping_id;
    }

    // ___________________________________________________

    public int getFlavor_id() {
        return flavor_id;
    }

    // ___________________________________________________

    public void setFlavor_id(int flavor_id) {
        this.flavor_id = flavor_id;
    }

} // Product end
