// src/main/java/com/scd/notification/service/NotificationListener.java
package com.scd.notification.service;

import com.scd.notification.model.InventoryEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {
    
    @KafkaListener(
        topics = "inventory-events", 
        groupId = "notification-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleInventoryEvent(InventoryEvent event) {
        // Simula envio de notificação
        System.out.printf(
            "Notification: Pedido %s está com status '%s'. Enviando e-mail/SMS...%n",
            event.getOrderId(),
            event.getStatus()
        );
    }
}
