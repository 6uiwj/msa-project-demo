package com.example.orchestration.infrastructure.kafka.producer;


import com.example.orchestration.application.dto.request.OrderRequestDto;
import com.example.orchestration.domain.DomainEvent;
import com.example.orchestration.infrastructure.dto.OrderCreateSuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

   public OrderRequestDto publishOrderCreateCommand(String topic, OrderRequestDto orderRequestDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(orderRequestDto);
            if(jsonString == null || jsonString.equals("")) {
                log.info("오케스트레이터에서 주문생성 이벤트 발행 중 메시지를 담지 못함");
                throw new RuntimeException("오케스트레이터에서 주문생성 이벤트 발행 중 메시지를 담지 못함");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonString);
       log.info("kafka Producer sent data from orchestration microservice");
        return orderRequestDto;
    }

//    public void publishOrderCreateCommand(UUID orderId) {
//        System.out.println("Kafka 발행: OrderCreateCommand, orderId=" + orderId);
//        kafkaTemplate.send("order-create", orderId.toString());
//    }

    public OrderCreateSuccessResponse publishStockReserveCommand(String topic, OrderCreateSuccessResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(response);
            if (jsonString == null || jsonString.equals("")) {
                log.info("오케스트레이터에서 재고차감 이벤트 발행 중 메시지를 담지 못함");
                throw new RuntimeException("오케스트레이터에서 재고차감 이벤트 발행 중 메시지를 담지 못함");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonString);
        System.out.println("Kafka 발행: ReserveStockCommand");
        return response;

    }

    public void publishPaymentCreateCommand(UUID orderId) {
        System.out.println("Kafka 발행: CreatePaymentCommand, orderId=" + orderId);
        kafkaTemplate.send("payment-create", orderId.toString());
    }

    public void publishSagaEvent(DomainEvent event) {
        kafkaTemplate.send("saga-event", event.getOrderId().toString(), event.toString());
    }
}