package com.example.orchestration.infrastructure.repository;

import com.example.orchestration.domain.SagaState;
import com.example.orchestration.domain.repository.SagaStateRepository;
import com.example.orchestration.domain.SagaStatus;
import com.example.orchestration.domain.SagaStep;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaStateRepositoryImpl implements SagaStateRepository {

    private final SagaStateJpaRepository sagaStateJpaRepository;

    @Override
    public Optional<SagaState> findBySagaId(UUID sagaId) {
        return sagaStateJpaRepository.findById(sagaId);
    }

    @Override
    public Optional<SagaState> findByOrderId(UUID orderId) {
        return sagaStateJpaRepository.findByOrderId(orderId);
    }

    @Override
    public List<SagaState> findByStatusAndCreatedAtBefore(SagaStatus sagaStatus,
        LocalDateTime cutOffTime) {
        return sagaStateJpaRepository.findByStatusAndCreatedAtBefore(sagaStatus, cutOffTime);
    }

    @Override
    public Optional<SagaState> findForIdempotencyCheck(String orderId, SagaStep step) {
        return sagaStateJpaRepository.findForIdempotencyCheck(orderId, step);
    }

    @Override
    public SagaState save(SagaState sagaState) {
        return sagaStateJpaRepository.save(sagaState);
    }

    @Override
    public SagaStatus findStatusBySagaId(UUID sagaId) {
        return sagaStateJpaRepository.findStatusBySagaId(sagaId);
    }
}
