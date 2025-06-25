package com.scd.inventory;

public class OrderItem {
    private String sku;
    private int quantity;

    public OrderItem() {}

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
