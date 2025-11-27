package com.sparta.product.application.dto.request;

import com.sparta.product.domain.entity.Product;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@EqualsAndHashCode(callSuper = false) //테스트를 위해
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA 또는 Jackson이 리플렉션을 사용해 객체를 생성하도록
@Getter
@Setter
public class ProductDto {
    private String name;
    private int price;
    private int stock;


    //향후 확장을 고려해  (특정 필드 초기화, 일부 필드를 기본값으로 채우고싶을 떄) 남김
    public ProductDto(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    //Dto를 엔티티로 변환하는 기능
    // 객체 생성 시이므로 create=modified = localdatetime.now
    public Product toEntity() {
        return Product.create(
            this.name,
            this.price,
            this.stock
        );
    }
}
