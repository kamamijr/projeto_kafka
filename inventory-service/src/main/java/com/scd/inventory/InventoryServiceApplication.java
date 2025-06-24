package com.scd.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
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

    /**
     * Listener para o tópico "orders". Recebe um OrderDTO local.
     */
    @KafkaListener(
        topics = "orders",
        groupId = "inventory-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrder(OrderDTO order) {
        // Simula reserva: 80% de sucesso
        boolean success = random.nextDouble() < 0.8;
        InventoryEvent event = new InventoryEvent(
            order.getId(),
            success ? "SUCCESS" : "FAILURE",
            order.getItems()
        );
        kafka.send("inventory-events", event.getOrderId(), event);
        System.out.printf(
            "Inventory-Service: orderId=%s, status=%s%n",
            event.getOrderId(),
            event.getStatus()
        );
    }

    /**
     * DTO local que espelha o payload publicado pelo Order-Service.
     */
    public static class OrderDTO {
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

    /**
     * Representa um item dentro de um pedido.
     */
    public static class OrderItem {
        private String sku;
        private int qty;

        public OrderItem() {}

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public int getQty() { return qty; }
        public void setQty(int qty) { this.qty = qty; }
    }

    /**
     * Evento de saída: resultado da reserva de estoque.
     */
    public static class InventoryEvent {
        private String orderId;
        private String status; // "SUCCESS" ou "FAILURE"
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
}
