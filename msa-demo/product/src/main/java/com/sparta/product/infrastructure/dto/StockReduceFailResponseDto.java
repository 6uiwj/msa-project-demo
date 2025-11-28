package com.sparta.product.infrastructure.dto;

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
public class StockReduceFailResponseDto {
    private UUID sagaId;
    private UUID productId;
    private int quantity;
    private String reason;
    private UUID orderId;
}
