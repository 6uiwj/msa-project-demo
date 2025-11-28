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

    @KafkaListener(topics = "order-success-create")
    public void orderCreateSuccess(String orderCreateSuccessResponse) {
        log.info("orderCreateSuccessResponse:{}", orderCreateSuccessResponse);

        OrderCreateSuccessResponse response = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            response = mapper.readValue(orderCreateSuccessResponse, OrderCreateSuccessResponse.class);
            orderSagaService.handlerOrderCreateSuccess(response);

        } catch (Exception e) {
            log.error("주문 성공 메시지 파싱 실패 : {}", orderCreateSuccessResponse, e);
            e.printStackTrace();
        }


        System.out.println("주문 요청 후 성공 메시지 받기 성공 ");

    }

    //TODO: 주문 생성 실패 이벤트 구독 로직 -> 주문 생성 실패시 클라이언트에 실패 메시지 반환?

    @KafkaListener(topics = "stock-reserve-success")
    public void stockReserveSuccess(String stockMessage) {
        log.info("stockReserveSuccess:{}", stockMessage);

        StockReserveSuccessResponseDto responseDto = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            responseDto = mapper.readValue(stockMessage, StockReserveSuccessResponseDto.class);

        } catch (Exception e) {
            log.error("재고 차감 성공 메시지 파싱 실패");
        }

        System.out.println("재고 차감 후 메시지 받기 성공");
    }

}
