package com.example.orchestration.domain;

public enum SagaStatus {
    STARTED,        // Saga 시작
    IN_PROGRESS,    // 진행 중
    COMPLETED,      // 성공 완료
    COMPENSATING,   // 보상 트랜잭션 진행 중
    COMPENSATED,    // 보상 완료 (롤백됨)
    FAILED          // 실패
}
