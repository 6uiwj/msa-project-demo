package com.sparta.product.domain.repository;

import com.sparta.product.domain.entity.Product;
import java.util.UUID;

public interface ProductRepository {
    //JPA save에는 이미 예외를 런타임 예외로 던짐 -> 언체크 예외
    Product insertProduct(Product product);

    //데이터 없으면 NosSuchElementException (언체크)
    Product selectProduct(UUID productId);

    //업데이트 로직에서 검증 실패나 다른 처리 문제 발생 가능 -> 체크 예외
    Product updateProduct(UUID productId, String name);

    //삭제하려는 엔티티가 존재하지 않거나 DB오류 발생 가능하므로 체크 예외 선언
    void deleteProduct(UUID productId);

    Product saveAndFlushProduct(Product product);
}
