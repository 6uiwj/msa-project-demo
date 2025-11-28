package com.example.orchestration.domain.repository;

import com.example.orchestration.domain.SagaState;
import com.example.orchestration.domain.SagaStatus;
import com.example.orchestration.domain.SagaStep;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SagaStateRepository {

    Optional<SagaState> findBySagaId(UUID sagaId);

    Optional<SagaState> findByOrderId(UUID orderId);

    List<SagaState> findByStatusAndCreatedAtBefore(
        SagaStatus sagaStatus, LocalDateTime cutOffTime
    );

    Optional<SagaState> findForIdempotencyCheck(String orderId, SagaStep step);

    SagaState save(SagaState sagaState);

    SagaStatus findStatusBySagaId(UUID sagaId);
}
