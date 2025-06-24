// src/main/java/com/scd/order/model/OrderItem.java  
package com.scd.order.model;

public class OrderItem {
    private String sku;
    private int qty;
    
    public OrderItem() {}
    
    public OrderItem(String sku, int qty) {
        this.sku = sku;
        this.qty = qty;
    }
    
    // Getters e Setters
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
}
