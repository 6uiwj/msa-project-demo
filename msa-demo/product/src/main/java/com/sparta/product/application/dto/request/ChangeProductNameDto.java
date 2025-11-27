package com.sparta.product.application.dto.request;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeProductNameDto {
    private UUID productId;
    private String productName;
    private String name;
}
