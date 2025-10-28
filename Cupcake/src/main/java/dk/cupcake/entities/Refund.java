package dk.cupcake.entities;

import java.sql.Timestamp;

public class Refund {

    //Attribues
    private int id;
    private int orderId;
    private int userId;
    private String reason;
    private Timestamp createdAt;
    private String status;

    // ______________________________________________________________

    public Refund(int id, int orderId, int userId, String reason, Timestamp createdAt, String status) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.reason = reason;
        this.createdAt = createdAt;
        this.status = status;
    }

    // ______________________________________________________________

    public int getId() {
        return id;
    }

    // ______________________________________________________________

    public void setId(int id) {
        this.id = id;
    }

    // ______________________________________________________________

    public int getOrderId() {
        return orderId;
    }

    // ______________________________________________________________

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    // ______________________________________________________________

    public int getUserId() {
        return userId;
    }

    // ______________________________________________________________

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // ______________________________________________________________

    public String getReason() {
        return reason;
    }

    // ______________________________________________________________

    public void setReason(String reason) {
        this.reason = reason;
    }

    // ______________________________________________________________

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // ______________________________________________________________

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // ______________________________________________________________

    public String getStatus() {
        return status;
    }

    // ______________________________________________________________

    public void setStatus(String status) {
        this.status = status;
    }
}
