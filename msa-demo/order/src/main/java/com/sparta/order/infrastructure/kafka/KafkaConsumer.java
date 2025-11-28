package com.sparta.order.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.order.application.OrderService;
import com.sparta.order.infrastructure.dto.OrderCreateFailedResponse;
import com.sparta.order.infrastructure.dto.OrderCreateSuccessResponse;
import com.sparta.order.infrastructure.dto.OrderDeleteMessage;
import com.sparta.order.infrastructure.dto.OrderDeleteSuccessResponse;
import com.sparta.order.infrastructure.dto.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    private final KafkaProducer kafkaProducer;
    private final OrderService orderService;

    public KafkaConsumer( KafkaProducer kafkaProducer,  OrderService orderService) {
        this.kafkaProducer = kafkaProducer;
        this.orderService = orderService;
    }

    //주문 생성 요청 메시지 구독
    @KafkaListener(topics = "order-create-request")
    public void createOrder(String kafkaMessage) {

        log.info("Kafka Message : {}", kafkaMessage);
        OrderMessage orderMessage = null;
        ObjectMapper mapper = new ObjectMapper();

        //메시지 파싱
        try {
            orderMessage = mapper.readValue(kafkaMessage, OrderMessage.class);
            if(orderMessage == null) {
                throw new RuntimeException("카프카 메시지 내용이 비어있음");
            }
            log.info("Kafka 메시지 구독 성공");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //주문 생성 로직 실행

        try {
            log.info("주문 생성 로직 실행");
            OrderCreateSuccessResponse response = orderService.createOrder(orderMessage);

            //주문 생성 성공 메시지 발행
            kafkaProducer.sendSuccess("order-success-create", response);

        } catch (Exception e) {
            OrderCreateFailedResponse orderCreateFailedResponse = new OrderCreateFailedResponse();
            orderCreateFailedResponse.setSagaId(orderMessage.getSagaId());
            orderCreateFailedResponse.setProductId(orderMessage.getProductId());
            orderCreateFailedResponse.setQuantity(orderMessage.getQuantity());
            orderCreateFailedResponse.setReason("몰랑..주문실패했엉..");
            kafkaProducer.sendFail("order-create-fail", orderCreateFailedResponse);
        }

    }


    @KafkaListener(topics = "order-cancel-request")
    public void cancelOrder(String kafkaMessage) {
        log.info("Kafka Message : {}", kafkaMessage);
        OrderDeleteMessage orderDeleteMessage = null;
        ObjectMapper mapper = new ObjectMapper();

        //메시지 파싱
        try {
            orderDeleteMessage = mapper.readValue(kafkaMessage, OrderDeleteMessage.class);
            if(orderDeleteMessage == null) {
                throw new RuntimeException("카프카 메시지 내용이 비어있음");
            }
            log.info("Kafka 메시지 구독 성공");

            log.info("주문 취소 로직 실행");
            orderService.deleteOrder(orderDeleteMessage);
            OrderDeleteSuccessResponse orderDeleteSuccessResponse =
                OrderDeleteSuccessResponse.builder()
                    .OrderId(orderDeleteMessage.getOrderId())
                    .SagaId(orderDeleteMessage.getSagaId())
                    .build();
            //주문 생성 성공 메시지 발행
            kafkaProducer.sendDeleteSuccess("order-delete-success", orderDeleteSuccessResponse);

        } catch (JsonProcessingException e) {
            log.error("주문 취소 성공 메시지 못보냄 ...어쩌냐...ㅋㅋ");
            e.printStackTrace();
        }


    }
}
