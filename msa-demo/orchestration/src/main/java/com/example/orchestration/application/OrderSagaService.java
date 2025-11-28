package com.example.orchestration.application;

import com.example.orchestration.application.dto.request.OrderRequestDto;
import com.example.orchestration.infrastructure.dto.OrderCreateSuccessResponse;
import com.example.orchestration.infrastructure.kafka.producer.KafkaEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class OrderSagaService {

    private final KafkaEventPublisher publisher;

    public OrderSagaService(KafkaEventPublisher publisher) {
        this.publisher = publisher;
    }

    // 1. Saga 시작: 주문 생성 Command 발행
    public void startSaga(OrderRequestDto orderRequestDto) {
        System.out.println("Saga 시작");
        publisher.publishOrderCreateCommand("order-create-request", orderRequestDto);
    }

    public void handlerOrderCreateSuccess(OrderCreateSuccessResponse response) {
        System.out.println("OrderSagaService: 주문 성공 이벤트 수신, 재고 차감 명령 발행");
        publisher.publishStockReserveCommand("stock-reduce-request", response);
        System.out.println("재고차감 명령 발행 성공");
    }
}
