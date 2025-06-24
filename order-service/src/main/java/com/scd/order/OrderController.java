// src/main/java/com/scd/order/controller/OrderController.java
package com.scd.order.controller;

import com.scd.order.dto.OrderRequest;
import com.scd.order.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    private final KafkaTemplate<String, Order> kafka;
    
    public OrderController(KafkaTemplate<String, Order> kafka) {
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
}