package com.scd.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@RestController
@RequestMapping("/orders")
public class OrderServiceApplication {

    private final KafkaTemplate<String, Order> kafka;

    public OrderServiceApplication(KafkaTemplate<String, Order> kafka) {
        this.kafka = kafka;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest req) {
        Order order = new Order(
            UUID.randomUUID().toString(),
            Instant.now().toString(),
            req.getItems()
        );
        kafka.send("orders", order.getId(), order);
        return order;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    // DTOs

    public static class OrderRequest {
        private List<OrderItem> items;
        public List<OrderItem> getItems() { return items; }
        public void setItems(List<OrderItem> items) { this.items = items; }
    }

    public static class Order {
        private String id;
        private String timestamp;
        private List<OrderItem> items;

        public Order() {}
        public Order(String id, String timestamp, List<OrderItem> items) {
            this.id = id;
            this.timestamp = timestamp;
            this.items = items;
        }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public List<OrderItem> getItems() { return items; }
        public void setItems(List<OrderItem> items) { this.items = items; }
    }

    public static class OrderItem {
        private String sku;
        private int qty;
        public OrderItem() {}
        public OrderItem(String sku, int qty) {
            this.sku = sku;
            this.qty = qty;
        }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public int getQty() { return qty; }
        public void setQty(int qty) { this.qty = qty; }
    }
}
