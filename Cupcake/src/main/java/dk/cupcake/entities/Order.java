// Package
package dk.cupcake.entities;

// Imports
import dk.cupcake.mapper.OrderItemMapper;
import dk.cupcake.mapper.OrderMapper;
import dk.cupcake.mapper.UserMapper;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;

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
    OrderItemMapper OrderItemMapper = new OrderItemMapper();

    public Order(int id, int userID) throws SQLException {
        UserMapper userMapper = new UserMapper();
        this.id = id;
        this.user = userMapper.getById(userID);
        this.status = "open";
        this.createdAt = Timestamp.from(Instant.now());
        this.items = new ArrayList<>();
    }

    //Hvis man vil lave en order og sætte attributter efterfølgende (???) JONAS
    public Order() {};

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
        boolean found = false;

        for (OrderItem item : items) {
            if (item.getProductId() == newItem.getProductId()) {

                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                found = true;


                OrderItemMapper.updateQuantity(orderID, item.getProductId(), item.getQuantity());
                break;
            }
        }

        if (!found) {
            OrderItemMapper.addOrderItem(orderID, newItem);
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
                    OrderItemMapper.deleteAmount(orderID, productId, amount);
                } else {
                    items.remove(i);
                    OrderItemMapper.deleteOrderItem(orderID, productId);
                }
                return;
            }
        }
    }

} // Order end
