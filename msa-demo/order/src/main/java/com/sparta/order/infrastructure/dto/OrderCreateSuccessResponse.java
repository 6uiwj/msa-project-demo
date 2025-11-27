package com.sparta.order.infrastructure.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateSuccessResponse {
    UUID productId;
    int quantity;
    UUID orderId;
}
