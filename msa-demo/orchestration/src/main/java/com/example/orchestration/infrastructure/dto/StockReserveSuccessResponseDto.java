package com.example.orchestration.infrastructure.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class StockReserveSuccessResponseDto {
    private UUID productId;
    private String name;
    private int price;
    private UUID orderId;
    private int quantity;
    private UUID sagaId;
}


