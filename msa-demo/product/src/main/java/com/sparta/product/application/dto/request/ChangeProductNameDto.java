package com.sparta.product.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeProductNameDto {
    private Long number;
    private String name;
}
