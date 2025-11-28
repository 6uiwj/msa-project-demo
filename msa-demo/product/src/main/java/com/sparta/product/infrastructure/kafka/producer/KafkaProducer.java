package com.sparta.product.infrastructure.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.product.infrastructure.dto.StockReduceSuccessResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public StockReduceSuccessResponseDto send(String topic, StockReduceSuccessResponseDto responseDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";

        try{
            jsonString = objectMapper.writeValueAsString(responseDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonString);
        log.info("[Product] Sent Order Created Successfully : {}", responseDto.getSagaId());

        return responseDto;
    }
}
