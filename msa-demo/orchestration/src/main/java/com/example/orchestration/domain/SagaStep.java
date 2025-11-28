package com.example.orchestration.domain;

public enum SagaStep {
    ORDER_CREATE,       // 주문 생성
    STOCK_RESERVE,      // 재고 차감
    PAYMENT_PROCESS,    // 결제 처리
    COMPLETED           // 완료
}
