package dk.cupcake.entities;

import java.sql.Timestamp;

public class Coupon {
    private int id;
    private String code;
    private int discountPercent;
    private Timestamp validFrom;
    private Timestamp validUntil;

    // ___________________________________________________

    public Coupon(int id, String code, int discountPercent, Timestamp validFrom, Timestamp validUntil) {
        this.id = id;
        this.code = code;
        this.discountPercent = discountPercent;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    // ___________________________________________________

    public void setId(int id) {
        this.id = id;
    }

    // ___________________________________________________

    public void setCode(String code) {
        this.code = code;
    }

    // ___________________________________________________

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    // ___________________________________________________

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    // ___________________________________________________

    public void setValidUntil(Timestamp validUntil) {
        this.validUntil = validUntil;
    }

    // ___________________________________________________

    public int getId() {
        return id;
    }

    // ___________________________________________________

    public String getCode() {
        return code;
    }

    // ___________________________________________________

    public int getDiscountPercent() {
        return discountPercent;
    }

    // ___________________________________________________

    public Timestamp getValidFrom() {
        return validFrom;
    }

    // ___________________________________________________

    public Timestamp getValidUntil() {
        return validUntil;
    }

}
