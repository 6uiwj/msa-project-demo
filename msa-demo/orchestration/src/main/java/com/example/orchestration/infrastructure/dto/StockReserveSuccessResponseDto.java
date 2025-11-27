package com.example.orchestration.infrastructure.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class StockReserveSuccessResponseDto {
    UUID productId;
    String name;
    int price;
    int stock;
}
