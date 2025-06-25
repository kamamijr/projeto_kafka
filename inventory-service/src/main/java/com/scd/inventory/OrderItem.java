package com.scd.inventory;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItem {
    private String sku;
    @JsonProperty("qty")
    private int quantity;

    public OrderItem() {}

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

