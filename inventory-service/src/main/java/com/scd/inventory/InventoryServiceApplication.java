package com.scd.inventory;

import com.scd.inventory.model.InventoryItem;
import com.scd.inventory.repository.InventoryItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@SpringBootApplication
public class InventoryServiceApplication {

    private final KafkaTemplate<String, InventoryEvent> kafka;
    private final InventoryItemRepository repo;

    public InventoryServiceApplication(
        KafkaTemplate<String, InventoryEvent> kafka,
        InventoryItemRepository repo
    ) {
        this.kafka = kafka;
        this.repo = repo;
    }

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @KafkaListener(
        topics = "orders",
        groupId = "inventory-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleOrder(OrderDTO order) {
        boolean allAvailable = order.getItems().stream()
            .allMatch(oi -> {
                InventoryItem item = repo.findById(oi.getSku()).orElse(null);
                return item != null && item.getQuantity() >= oi.getQuantity();
            });

        if (allAvailable) {
            order.getItems().forEach(oi -> {
                InventoryItem item = repo.findById(oi.getSku()).get();
                item.setQuantity(item.getQuantity() - oi.getQuantity());
                repo.save(item);
            });
        }

        // Em qualquer caso, gera o evento e publica no Kafka
        InventoryEvent event = new InventoryEvent(
            order.getId(),
            allAvailable ? "SUCCESS" : "FAILURE",
            order.getItems()
        );
        kafka.send("inventory-events", event.getOrderId(), event);

        System.out.printf(
          "Inventory-Service: processado orderId=%s → %s%n",
          order.getId(),
          allAvailable ? "SUCCESS" : "FAILURE"
        );
    }
}
