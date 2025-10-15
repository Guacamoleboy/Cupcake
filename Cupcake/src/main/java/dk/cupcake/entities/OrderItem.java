// Package
package dk.cupcake.entities;

// Imports

public class OrderItem {

    // Attributes
    private int id;
    // private int orderId;
    private int productId;
    private int bottomId;
    private int toppingId;
    private int quantity;
    private String title;
    private String description;
    private double price;

    public OrderItem(int productId, String title, String description, double price, int quantity, int toppingId, int bottomId) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.toppingId = toppingId;
        this.bottomId = bottomId;
    }

    // ___________________________________________________

    public int getQuantity() {
        return quantity;
    }

    // ___________________________________________________

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // ___________________________________________________

    /*public int getOrderId() {
        return orderId;
    }

    // ___________________________________________________

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }*/

    // ___________________________________________________

    public int getProductId() {
        return productId;
    }

    // ___________________________________________________

    public void setProductId(int productId) {
        this.productId = productId;
    }

    // ___________________________________________________

    public int getBottomId() {
        return bottomId;
    }

    // ___________________________________________________

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    // ___________________________________________________

    public int getToppingId() {
        return toppingId;
    }

    // ___________________________________________________

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
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

    public String getTitle() {
        return title;
    }

    // ___________________________________________________

    public void setTitle(String title) {
        this.title = title;
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
} // OrderItem end