package com.sparta.order.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("주문 생성"),
    CANCELED("주문 취소"),
    SUCCESS("주문 완료"),
    RETURN_REQUEST("반품 요청"),
    RETURNED("반품 완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
