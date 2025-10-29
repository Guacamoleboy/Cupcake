// Package
package dk.cupcake.entities;

// Imports
import dk.cupcake.mapper.OrderItemMapper;
import dk.cupcake.mapper.UserMapper;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

public class Order {

    // Attributes
    private int id;
    private User user;
    private String status;
    private Timestamp createdAt;
    private ArrayList<OrderItem> items;
    private Integer deliveryMethodId;
    private Integer paymentMethodId;
    private String deliveryAddress;
    private double totalPrice;
    OrderItemMapper OrderItemMapper = new OrderItemMapper();

    // ___________________________________________________

    public Order(int id, int userID) throws SQLException {
        UserMapper userMapper = new UserMapper();
        this.id = id;
        this.user = userMapper.getById(userID);
        this.status = "open";
        this.createdAt = Timestamp.from(Instant.now());
        this.items = new ArrayList<>();
    }

// ___________________________________________________

    public Order() {
        this.items = new ArrayList<>();
        this.status = "open";
        this.createdAt = Timestamp.from(Instant.now());
    }

    // ___________________________________________________

    public Integer getDeliveryMethodId() {
        return deliveryMethodId;
    }

    // ___________________________________________________

    public void setDeliveryMethodId(Integer deliveryMethodId) {
        this.deliveryMethodId = deliveryMethodId;
    }

    // ___________________________________________________

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    // ___________________________________________________

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    // ___________________________________________________

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // ___________________________________________________

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

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

    // ___________________________________________________

    public void addToOrder(OrderItem newItem, int orderID) throws SQLException {

        if (items == null) items = new ArrayList<>();

        boolean found = false;

        for (OrderItem item : items) {
            if (item.isMergeableWith(newItem)) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                found = true;

                if (orderID > 0) {
                    if (item.getId() > 0) {
                        OrderItemMapper.updateQuantity(orderID, item.getId(), item.getQuantity());
                    } else {
                        OrderItemMapper.addOrderItem(orderID, item);
                    }
                }
                break;
            }
        }

        if (!found) {
            if (orderID > 0) {
                OrderItemMapper.addOrderItem(orderID, newItem);
            }
            items.add(newItem);
        }
    }


    // ___________________________________________________

    public void removeFromOrder(int productId, int amount, int orderID) throws SQLException {
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);

            if (item.getProductId() == productId) {
                int newQuantity = item.getQuantity() - amount;

                if (newQuantity > 0) {
                    item.setQuantity(newQuantity);
                    OrderItemMapper.deleteAmount(orderID, item.getId(), amount);
                } else {
                    items.remove(i);
                    OrderItemMapper.deleteOrderItem(orderID, item.getId());
                }
                return;
            }
        }
    }

    // ___________________________________________________

    public double getTotalPrice() {
        return this.totalPrice;
    }

    // ___________________________________________________

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }



} // Order end
