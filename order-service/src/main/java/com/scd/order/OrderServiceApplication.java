package com.scd.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
}
