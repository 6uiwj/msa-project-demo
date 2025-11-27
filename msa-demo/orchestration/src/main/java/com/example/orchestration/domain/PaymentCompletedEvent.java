package com.example.orchestration.domain;

import java.util.UUID;

public class PaymentCompletedEvent extends DomainEvent{
    private final UUID orderId;
    public PaymentCompletedEvent(UUID orderId) { this.orderId = orderId; }
    @Override
    public UUID getOrderId() {
        return orderId;
    }
}
