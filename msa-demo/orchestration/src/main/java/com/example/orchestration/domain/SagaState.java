package com.example.orchestration.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//SAGA 상태 관리 엔티티
@Entity
@Table(name = "saga_state")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SagaState {

    @Id
    private UUID sagaId;

    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private SagaStatus status;

    @Enumerated(EnumType.STRING)
    private SagaStep currentStep;

    @Column(columnDefinition = "TEXT")
    private String payload; // 원본 요청 데이터 (보상 트랜잭션용)

    private String errorMessage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public SagaState(UUID sagaId, UUID orderId, SagaStatus status,
        SagaStep currentStep, String payload) {
        this.sagaId = sagaId;
        this.orderId = orderId;
        this.status = status;
        this.currentStep = currentStep;
        this.payload = payload;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStep(SagaStep step) {
        this.currentStep = step;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = SagaStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void fail(String errorMessage) {
        this.status = SagaStatus.FAILED;
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
    }

    public void startCompensation() {
        this.status = SagaStatus.COMPENSATING;
        this.updatedAt = LocalDateTime.now();
    }

    public void compensated() {
        this.status = SagaStatus.COMPENSATED;
        this.updatedAt = LocalDateTime.now();
    }
}
