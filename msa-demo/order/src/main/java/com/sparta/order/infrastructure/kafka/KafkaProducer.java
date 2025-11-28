package com.sparta.order.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.order.infrastructure.dto.OrderCreateFailedResponse;
import com.sparta.order.infrastructure.dto.OrderCreateSuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderCreateSuccessResponse sendSuccess(String topic, OrderCreateSuccessResponse orderCreateResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";

        try{
            jsonString = objectMapper.writeValueAsString(orderCreateResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonString);
        log.info("[Order] Sent Order Created Successfully");

        return orderCreateResponse;
    }

    public OrderCreateFailedResponse sendFail(String topic, OrderCreateFailedResponse orderCreateFailedResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";

        try {
            jsonString = objectMapper.writeValueAsString(orderCreateFailedResponse);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(topic, jsonString);
        log.info("Sent Order Creation failed ");

        return orderCreateFailedResponse;
    }
}
