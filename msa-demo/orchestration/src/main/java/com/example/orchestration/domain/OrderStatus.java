package com.example.orchestration.domain;

import java.util.UUID;

public enum OrderStatus {
    CREATED,      // 주문 생성됨
    STOCK_RESERVED, // 재고 차감됨
    PAYMENT_COMPLETED, // 결제 완료
    COMPLETED,    // 전체 Saga 완료
    CANCELLED     // 보상 트랜잭션 실행
}
