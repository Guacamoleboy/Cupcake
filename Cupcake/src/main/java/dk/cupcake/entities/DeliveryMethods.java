// Package
package dk.cupcake.entities;

public class DeliveryMethods {

    // Attributes
    private int id;
    private String name;
    private double price;

    // _______________________________________________

    public DeliveryMethods() {}

    // _______________________________________________

    public DeliveryMethods(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // _______________________________________________

    public int getId() {
        return id;
    }

    // _______________________________________________

    public void setId(int id) {
        this.id = id;
    }

    // _______________________________________________

    public String getName() {
        return name;
    }

    // _______________________________________________

    public void setName(String name) {
        this.name = name;
    }

    // _______________________________________________

    public double getPrice() {
        return price;
    }

    // _______________________________________________

    public void setPrice(double price) {
        this.price = price;
    }

} // DeliveryMethods end