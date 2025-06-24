package com.yourorg.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "inventory-events", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleInventoryEvent(InventoryEvent event) {
        // Simula envio de notificação
        System.out.printf(
            "Notification: Pedido %s está com status '%s'. Enviando e-mail/SMS...%n",
            event.getOrderId(),
            event.getStatus()
        );
    }

    // Deve espelhar a classe InventoryEvent do Inventory-Service
    public static class InventoryEvent {
        private String orderId;
        private String status;
        // getters/setters omitidos

        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
