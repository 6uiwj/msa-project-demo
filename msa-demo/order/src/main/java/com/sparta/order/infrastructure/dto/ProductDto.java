package com.sparta.order.infrastructure.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

    UUID productId;
    String name;
    int price;
    int stock;
}
