package com.example.orchestration.domain;

import java.util.UUID;

public class OrderCreatedEvent extends DomainEvent {

    private final UUID orderId;

    public OrderCreatedEvent(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public UUID getOrderId() {
        return orderId;
    }
}
