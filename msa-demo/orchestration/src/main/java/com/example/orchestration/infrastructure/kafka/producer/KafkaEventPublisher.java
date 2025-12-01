package com.example.orchestration.infrastructure.kafka.producer;


import com.example.orchestration.application.dto.request.OrderRequestDto;
import com.example.orchestration.infrastructure.dto.OrderCancelCommand;
import com.example.orchestration.infrastructure.dto.OrderCreateSuccessResponse;
import com.example.orchestration.infrastructure.dto.StockReserveSuccessResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    //공통 발행 메서드
    private String toJson(Object obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            if (json == null || json.isEmpty()) {
                throw new RuntimeException("Kafka 메시지 직렬화 실패: 빈 데이터");
            }
            return json;
        } catch (Exception e) {
            log.error("Kafka 메시지 JSON 변환 실패: {}", obj, e);
            throw new RuntimeException("Kafka 메시지 직렬화 오류", e);
        }
    }

    private void send(String topic, Object messageObj) {
        String json = toJson(messageObj);
        kafkaTemplate.send(topic, json);
        log.info("[Kafka Publish] topic={}, message={}", topic, json);
    }


    //주문 생성 요청 이벤트
   public void publishOrderCreateCommand(String topic, OrderRequestDto orderRequestDto) {
        send(topic, orderRequestDto);
    }

    //재고 차감 요청 이벤트
    public void publishStockReserveCommand(String topic, OrderCreateSuccessResponse response) {
        send(topic, response);
    }

    //구현 전
    public void publishPaymentCreateCommand(String topic, StockReserveSuccessResponseDto responseDto) {
        send(topic, responseDto);
    }

    public void publishOrderCancelCommand(String topic, OrderCancelCommand cancelCommand) {
        send(topic, cancelCommand);
    }
}