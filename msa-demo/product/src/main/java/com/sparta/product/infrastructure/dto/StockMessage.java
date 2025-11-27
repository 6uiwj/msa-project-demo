package com.sparta.product.infrastructure.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockMessage {
    UUID productId;
    int quantity;
    UUID orderId;
}
