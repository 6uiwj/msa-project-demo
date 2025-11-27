package com.example.orchestration.domain;

import java.util.UUID;

public class OrderCancelledEvent extends DomainEvent{
    private final UUID orderId;
    private final String reason;

    public OrderCancelledEvent(UUID orderId, String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }

    @Override
    public UUID getOrderId() {
        return orderId;
    }
    public String getReason() { return reason; }

}
