package com.example.orchestration.domain;

import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {
    private final Instant occurredAt = Instant.now();
    public Instant getOccurredAt() { return occurredAt; }
    public abstract UUID getOrderId();
}
