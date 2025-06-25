package com.scd.inventory;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class InventoryServiceKafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, OrderDTO> orderKafkaTemplate;

    @Test
    void shouldProcessOrderAndPublishInventoryEvent() throws Exception {
        // Monta um pedido
        OrderItem item = new OrderItem();
        item.setSku("ABC");
        item.setQuantity(2);
        OrderDTO order = new OrderDTO();
        order.setId("order-docker-1");
        order.setTimestamp("2025-06-25T10:00:00Z");
        order.setItems(List.of(item));

        // Publica no tópico 'orders' e aguarda confirmação
        orderKafkaTemplate.send("orders", order.getId(), order).get();

        // Aguarda um tempo para o InventoryService processar
        Thread.sleep(2000);

        // Configura consumidor para 'inventory-events' (usando Kafka real)
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put("bootstrap.servers", "localhost:9092");
        consumerProps.put("group.id", "testGroup");
        consumerProps.put("auto.offset.reset", "earliest");
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.scd.inventory.InventoryEvent");
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", JsonDeserializer.class);

        boolean found = false;
        try (KafkaConsumer<String, InventoryEvent> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(Collections.singletonList("inventory-events"));
            long end = System.currentTimeMillis() + 10000; // timeout de 10s
            while (System.currentTimeMillis() < end) {
                ConsumerRecords<String, InventoryEvent> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, InventoryEvent> record : records) {
                    InventoryEvent event = record.value();
                    if (event != null && order.getId().equals(event.getOrderId())) {
                        assertThat(event.getItems()).hasSize(1);
                        assertThat(event.getItems().get(0).getSku()).isEqualTo("ABC");
                        assertThat(event.getStatus()).isIn("SUCCESS", "FAILURE");
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
        }
        if (!found) {
            fail("InventoryEvent referente ao pedido não foi encontrado no tópico 'inventory-events'.");
        }
    }
}
