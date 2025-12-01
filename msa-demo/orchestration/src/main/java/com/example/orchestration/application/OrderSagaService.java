package com.example.orchestration.application;

import com.example.orchestration.application.dto.request.OrderRequestDto;
import com.example.orchestration.domain.SagaState;
import com.example.orchestration.domain.SagaStatus;
import com.example.orchestration.domain.SagaStep;
import com.example.orchestration.domain.repository.SagaStateRepository;
import com.example.orchestration.infrastructure.dto.OrderCancelCommand;
import com.example.orchestration.infrastructure.dto.OrderCreateFailedResponse;
import com.example.orchestration.infrastructure.dto.OrderCreateSuccessResponse;
import com.example.orchestration.infrastructure.dto.StockReserveFailedResponseDto;
import com.example.orchestration.infrastructure.dto.StockReserveSuccessResponseDto;
import com.example.orchestration.infrastructure.kafka.producer.KafkaEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderSagaService {

    private final KafkaEventPublisher publisher;
    private final ObjectMapper objectMapper;
    private final SagaStateRepository sagaStateRepository;


    // 1. Saga 시작: 주문 생성 Command 발행
    @Transactional
    public void startSaga(OrderRequestDto orderRequestDto)  {

        //주문 사가 레코드 생성
        UUID sagaId = UUID.randomUUID();
        try {
            orderRequestDto.setSagaId(sagaId);
            SagaState sagaState = SagaState.builder()
                .sagaId(sagaId)
                .orderId(null)
                .status(SagaStatus.STARTED)
                .currentStep(SagaStep.ORDER_CREATE)
                .payload(objectMapper.writeValueAsString(orderRequestDto))
                .build();

            sagaStateRepository.save(sagaState);

            log.info("[Saga Started] sagaId={}, productId={}, quantity={}",
                sagaId, orderRequestDto.getProductId(), orderRequestDto.getQuantity());

            //주문 생성 요청 이벤트 발행
            publisher.publishOrderCreateCommand("order-create-request", orderRequestDto);

        } catch (Exception e) {
            log.info("Saga 시작 실패, 주문 생성 요청 전송 실패 ");
            throw new RuntimeException("Saga 시작 실패");
        }
        System.out.println("Saga 시작");
    }
    //1단계: 주문 생성 성공
    @Transactional
    public void handlerOrderCreateSuccess(OrderCreateSuccessResponse response) {
        UUID sagaId = response.getSagaId();

        try {
            SagaState sagaState = getSagaState(sagaId);

            // 멱등성 체크 - 이미 처리됐다면 넘어가기
            if (sagaState.getCurrentStep().ordinal() > SagaStep.ORDER_CREATE.ordinal()) {
                log.warn("[Idempotency] 이미 처리된 이벤트: sagaId={}, currentStep={}",
                    sagaId, sagaState.getCurrentStep());
                return;
            }

            // SAGA 레코드 상태 업데이트 (orderId 저장)
            SagaState updatedState = SagaState.builder()
                .sagaId(sagaState.getSagaId())
                .orderId(response.getOrderId())
                .status(SagaStatus.IN_PROGRESS)
                .currentStep(SagaStep.STOCK_RESERVE)
                .payload(sagaState.getPayload())
                .build();
            sagaStateRepository.save(updatedState);

            log.info("[Order Created] sagaId={}, orderId={}", sagaId, response.getOrderId());


            // 재고 차감 명령 발행
            log.info("OrderSagaService: 주문 성공 이벤트 수신, 재고 차감 명령 발행 - sagaId : {}",response.getSagaId());
            publisher.publishStockReserveCommand("stock-reduce-request", response);
            System.out.println("재고차감 명령 발행 성공");

        } catch (Exception e) {
            log.error("주문 생성 성공 처리 실패: sagaId={}", sagaId, e);
        }
    }

    // ===== 1단계: 주문 생성 실패 =====
    @Transactional
    public void handleOrderCreateFailed(OrderCreateFailedResponse response) {
        UUID sagaId = response.getSagaId();

        try {
            SagaState sagaState = getSagaState(sagaId);
            sagaState.fail("주문 생성 실패: " + response.getReason());
            sagaStateRepository.save(sagaState);

            log.error("[Order Create Failed] sagaId={}, reason={}",
                sagaId, response.getReason());

            // 보상 트랜잭션 불필요 (1단계라 되돌릴게 없음)

        } catch (Exception e) {
            log.error("주문 생성 실패 처리 중 오류: sagaId={}", sagaId, e);
        }
    }

    //2단계 재고 차감 성공
    public void handleStockReduceSuccess(StockReserveSuccessResponseDto response) {
        UUID sagaId = response.getSagaId();

        try {
            SagaState sagaState = getSagaState(sagaId);

            // 멱등성 체크
            if (sagaState.getCurrentStep().ordinal() > SagaStep.STOCK_RESERVE.ordinal()) {
                log.warn("[Idempotency] 이미 처리된 이벤트: sagaId={}, currentStep={}",
                    sagaId, sagaState.getCurrentStep());
                return;
            }

            // Saga 완료 처리
            sagaState.complete();
            sagaStateRepository.save(sagaState);

            log.info("[Saga Completed] sagaId={}, orderId={}, productId={}",
                sagaId, response.getOrderId(), response.getProductId());


        } catch (Exception e) {
            log.error("재고 차감 성공 처리 실패: sagaId={}", sagaId, e);
        }

//        System.out.println("OrderSagaService: 재고 차감 성공 이벤트 수신, 결제 요청 명령 발행");
//        publisher.publishPaymentCreateCommand("payment-create-request", response);
    }


    // ===== 2단계: 재고 차감 실패 → 보상: 주문 취소 =====
    @Transactional
    public void handleStockReduceFailed(StockReserveFailedResponseDto response) {
        UUID sagaId = response.getSagaId();

        try {
            SagaState sagaState = getSagaState(sagaId);
            sagaState.startCompensation();
            sagaStateRepository.save(sagaState);

            log.error("[Stock Reserve Failed] sagaId={}, reason={}, orderId={} 보상 트랜잭션 시작",
                sagaId, response.getReason(), response.getOrderId());

            // 보상: 주문 취소 명령 발행
            OrderCancelCommand cancelCommand = OrderCancelCommand.builder()
                .sagaId(sagaId)
                .orderId(response.getOrderId())
                .reason("재고 부족: " + response.getReason())
                .build();

            publisher.publishOrderCancelCommand("order-cancel-request", cancelCommand);

        } catch (Exception e) {
            log.error("재고 차감 실패 처리 중 오류: sagaId={}", sagaId, e);
        }

        log.info("재고차감 실패 후 보상 트랜잭션까지 모두 완료");
    }

        public SagaState getSagaState(UUID sagaId) {
            return sagaStateRepository.findBySagaId(sagaId).orElseThrow(() -> new RuntimeException("saga 존재하지 않음"));
        }
}
