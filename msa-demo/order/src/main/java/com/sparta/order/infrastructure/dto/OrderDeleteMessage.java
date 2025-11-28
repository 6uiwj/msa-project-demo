package com.sparta.order.infrastructure.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDeleteMessage {
    private UUID sagaId;
    private UUID orderId;
    private String reason;
}
