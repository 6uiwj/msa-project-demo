package com.sparta.product.application.dto.response;

import com.sparta.product.domain.entity.Product;
import java.util.UUID;
import lombok.Getter;


@Getter
public class ProductResponseDto {
    private final UUID productId;
    private final String name;
    private final int price;
    private final int stock;

    private ProductResponseDto(UUID productId, String name, int price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    //테스트용 팩토리 메서드
    public static ProductResponseDto of(UUID productId, String name, int price, int stock) {
        return new ProductResponseDto(productId, name, price, stock);
    }



    //Entity로부터 변환하는 메서드이므로 Entity에서 값을 받아 DTO로 변환하기
    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
            product.getProductId(),
            product.getName(),
            product.getPrice(),
            product.getStock()
        );
    }

}
