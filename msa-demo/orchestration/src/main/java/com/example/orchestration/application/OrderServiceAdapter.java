package com.example.orchestration.application;

import com.example.orchestration.domain.OrderCreatedEvent;
import com.example.orchestration.infrastructure.kafka.producer.KafkaEventPublisher;
import java.util.UUID;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceAdapter {
    private final KafkaEventPublisher publisher;

    public OrderServiceAdapter(KafkaEventPublisher publisher) { this.publisher = publisher; }

    @KafkaListener(topics = "order-create", groupId = "order-service-group")
    public void handleOrderCreate(String orderIdStr) {
        UUID orderId = UUID.fromString(orderIdStr);
        System.out.println("주문 서비스 처리, orderId=" + orderId);
        publisher.publishSagaEvent(new OrderCreatedEvent(orderId));
    }
}
