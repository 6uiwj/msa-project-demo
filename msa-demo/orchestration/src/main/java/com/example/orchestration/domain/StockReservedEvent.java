package com.example.orchestration.domain;

import java.util.UUID;

public class StockReservedEvent  extends DomainEvent{

    private final UUID orderId;
    public StockReservedEvent(UUID orderId) { this.orderId = orderId; }

    @Override
    public UUID getOrderId() {
        return orderId;
    }
}
