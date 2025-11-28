package com.example.orchestration.infrastructure.kafka.consumer;

import com.example.orchestration.application.OrderSagaService;
import com.example.orchestration.infrastructure.dto.OrderCreateSuccessResponse;
import com.example.orchestration.infrastructure.dto.StockReserveSuccessResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final OrderSagaService orderSagaService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-success-create")
    public void orderCreateSuccess(String orderCreateSuccessResponse) {
        log.info("orderCreateSuccessResponse:{}", orderCreateSuccessResponse);


        try {
            OrderCreateSuccessResponse response = objectMapper.readValue(orderCreateSuccessResponse, OrderCreateSuccessResponse.class);
            orderSagaService.handlerOrderCreateSuccess(response);
            log.info("주문 생성 성공 이벤트 처리 완료");

        } catch (Exception e) {
            log.error("주문 성공 메시지 파싱 실패 : {}", orderCreateSuccessResponse, e);
        }

        System.out.println("주문 요청 후 성공 메시지 받기 성공 ");
    }

    //TODO: 주문 생성 실패 이벤트 구독 로직 -> 주문 생성 실패시 클라이언트에 실패 메시지 반환?

    @KafkaListener(topics = "stock-reserve-success")
    public void stockReserveSuccess(String stockMessage) {
        log.info("stockReserveSuccess:{}", stockMessage);

        try {
            StockReserveSuccessResponseDto responseDto = objectMapper.readValue(stockMessage, StockReserveSuccessResponseDto.class);
            orderSagaService.handleStockReduceSuccess(responseDto);
        } catch (Exception e) {
            log.error("재고 차감 성공 메시지 파싱 실패");
        }
        System.out.println("재고 차감 후 메시지 받기 성공");
    }

    //TODO: 재고 차감 실패 이벤트 구독 로직

}
