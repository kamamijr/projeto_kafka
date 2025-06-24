// src/main/java/com/scd/notification/model/InventoryEvent.java
package com.scd.notification.model;

public class InventoryEvent {
    private String orderId;
    private String status;
    
    public InventoryEvent() {}
    
    public InventoryEvent(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
    
    // Getters e Setters
    public String getOrderId() { 
        return orderId; 
    }
    
    public void setOrderId(String orderId) { 
        this.orderId = orderId; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
}