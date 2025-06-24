package com.scd.inventory;

import java.util.List;

public class InventoryEvent {
    private String orderId;
    private String status;
    private List<OrderItem> items;

    public InventoryEvent() {}

    public InventoryEvent(String orderId, String status, List<OrderItem> items) {
        this.orderId = orderId;
        this.status = status;
        this.items = items;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
