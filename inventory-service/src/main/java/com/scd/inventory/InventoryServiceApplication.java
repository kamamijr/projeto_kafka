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

    @KafkaListener(
        topics = "orders",
        groupId = "inventory-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrder(OrderDTO order) {
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
}
