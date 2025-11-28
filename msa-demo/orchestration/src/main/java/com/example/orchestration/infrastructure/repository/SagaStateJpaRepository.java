package com.example.orchestration.infrastructure.repository;

import com.example.orchestration.domain.SagaState;
import com.example.orchestration.domain.SagaStatus;
import com.example.orchestration.domain.SagaStep;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaStateJpaRepository extends JpaRepository<SagaState, UUID> {
    List<SagaState> findByStatusAndCreatedAtBefore(SagaStatus sagaStatus, LocalDateTime cutOffTime);

    @Query("SELECT s FROM SagaState s WHERE s.orderId = :orderId " +
        "AND s.currentStep = :step AND s.status = 'IN_PROGRESS'")
    Optional<SagaState> findForIdempotencyCheck(
        @Param("orderId") String orderId,
        @Param("step") SagaStep step
    );

    Optional<SagaState> findByOrderId(UUID orderId);

    @Query("select s.status from SagaState s where s.sagaId = :sagaId")
    SagaStatus findStatusBySagaId(UUID sagaId);
}
