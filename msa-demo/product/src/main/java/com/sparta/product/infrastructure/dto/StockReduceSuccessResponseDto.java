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
public class StockReduceSuccessResponseDto {
    private UUID productId;
    private String name;
    private int price;
    private UUID orderId;
    private int quantity;
    private UUID sagaId;
}
