package com.example.orchestration.application.dto.request;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRequestDto {
    private UUID productId;
    private int quantity;
}
