package com.sparta.order.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public OrderCreateSuccessResponse send(String topic, OrderCreateSuccessResponse orderCreateResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";

        try{
            jsonString = objectMapper.writeValueAsString(orderCreateResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonString);
        log.info("Sent Order Created Successfully");

        return orderCreateResponse;
    }
}
