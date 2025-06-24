// src/main/java/com/scd/order/model/Order.java
package com.scd.order.model;

import java.util.List;

public class Order {
    private String id;
    private String timestamp;
    private List<OrderItem> items;
    
    public Order() {}
    
    public Order(String id, String timestamp, List<OrderItem> items) {
        this.id = id;
        this.timestamp = timestamp;
        this.items = items;
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}