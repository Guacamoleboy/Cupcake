// Package
package dk.cupcake.entities;

public class PaymentMethods {

    // Attributes
    private int id;
    private String name;

    // _________________________________________________

    public PaymentMethods() {}

    // _________________________________________________

    public PaymentMethods(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // _________________________________________________

    public int getId() {
        return id;
    }

    // _________________________________________________

    public void setId(int id) {
        this.id = id;
    }

    // _________________________________________________

    public String getName() {
        return name;
    }

    // _________________________________________________

    public void setName(String name) {
        this.name = name;
    }

} // PaymentMethods end