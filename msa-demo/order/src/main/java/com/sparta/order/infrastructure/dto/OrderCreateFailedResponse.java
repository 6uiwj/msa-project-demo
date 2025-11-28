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
public class OrderCreateFailedResponse {
    private UUID sagaId;
    private UUID productId;
    private int quantity;
    private String reason;
}
