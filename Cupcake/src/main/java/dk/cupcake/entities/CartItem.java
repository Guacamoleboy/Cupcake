// Package
package dk.cupcake.entities;

// Imports

public class CartItem {

    // Attributes
    private Integer productId;
    private Integer flavorId;
    private Integer toppingId;
    private String name;
    private double unitPrice;
    private int quantity;
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getFlavorId() {
        return flavorId;
    }

    public void setFlavorId(Integer flavorId) {
        this.flavorId = flavorId;
    }

    public Integer getToppingId() {
        return toppingId;
    }

    public void setToppingId(Integer toppingId) {
        this.toppingId = toppingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getLineTotal() {
        return unitPrice * quantity;
    }

    public boolean isMergeableWith(CartItem other) {

        if (other == null) return false;
        boolean sameProduct = productId != null && productId.equals(other.productId);
        boolean sameCustom = productId == null && other.productId == null &&
                flavorId != null && flavorId.equals(other.flavorId) &&
                toppingId != null && toppingId.equals(other.toppingId);
        return sameProduct || sameCustom;
    }

} // CartItem end