package com.yourorg.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Random;

@SpringBootApplication
public class InventoryServiceApplication {

    private final KafkaTemplate<String, InventoryEvent> kafka;
    private final Random random = new Random();

    public InventoryServiceApplication(KafkaTemplate<String, InventoryEvent> kafka) {
        this.kafka = kafka;
    }

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @KafkaListener(topics = "orders", groupId = "inventory-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleOrder(OrderServiceApplication.Order order) {
        // Simula reserva: 80% de sucesso
        boolean success = random.nextDouble() < 0.8;
        InventoryEvent event = new InventoryEvent(
            order.getId(),
            success ? "SUCCESS" : "FAILURE",
            order.getItems()
        );
        kafka.send("inventory-events", event.getOrderId(), event);
        System.out.printf("Inventory: orderId=%s result=%s%n", event.getOrderId(), event.getStatus());
    }

    // DTO espelho de OrderItem
    public static class OrderItem {
        private String sku;
        private int qty;
        // getters/setters omitidos para brevidade
    }

    public static class InventoryEvent {
        private String orderId;
        private String status; // "SUCCESS" ou "FAILURE"
        private Object items;  // opcional: lista de itens

        public InventoryEvent() {}
        public InventoryEvent(String orderId, String status, Object items) {
            this.orderId = orderId;
            this.status = status;
            this.items = items;
        }
        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Object getItems() { return items; }
        public void setItems(Object items) { this.items = items; }
    }
}
