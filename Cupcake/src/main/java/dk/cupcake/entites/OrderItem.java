// Package
package dk.cupcake.entites;

// Imports

public class OrderItem {

    // Attributes
    private int id;
    private int orderId;
    private int productId;
    private int bottomId;
    private int toppingId;
    private int quantity;

    // ___________________________________________________

    public int getQuantity() {
        return quantity;
    }

    // ___________________________________________________

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // ___________________________________________________

    public int getOrderId() {
        return orderId;
    }

    // ___________________________________________________

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

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

} // OrderItem end