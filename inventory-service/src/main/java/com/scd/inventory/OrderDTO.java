package com.scd.inventory;

import java.util.List;

public class OrderDTO {
    private String id;
    private String timestamp;
    private List<OrderItem> items;

    public OrderDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
