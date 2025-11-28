package com.sparta.product.infrastructure.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.product.application.ProductService;
import com.sparta.product.infrastructure.dto.StockMessage;
import com.sparta.product.infrastructure.dto.StockReduceFailResponseDto;
import com.sparta.product.infrastructure.dto.StockReduceSuccessResponseDto;
import com.sparta.product.infrastructure.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ProductService productService;
    private final KafkaProducer kafkaProducer;

    //재고차감 요청 이벤트
    @KafkaListener(topics = "stock-reduce-request")
    public void stockReserveCommand(String kafkaMessage) {
        log.info("Received stock reserve command: " + kafkaMessage);

        StockMessage stockMessage = null;

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            stockMessage = objectMapper.readValue(kafkaMessage, StockMessage.class);
            if(stockMessage == null){
                throw new RuntimeException("stock reserve command received null");
            }
           log.info("메시지 담기 성공");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            StockReduceSuccessResponseDto responseDto = productService.reduceStock(stockMessage);
            kafkaProducer.sendSuccess("stock-reserve-success", responseDto);
            log.info("Sent stock reserve create message 전송 : {}", responseDto.getSagaId());

        } catch (Exception e) {
            int stock = productService.getProduct(stockMessage.getProductId()).getStock();
            StockReduceFailResponseDto responseDto =
                StockReduceFailResponseDto.builder()
                        .sagaId(stockMessage.getSagaId())
                        .productId(stockMessage.getProductId())
                        .quantity(stockMessage.getQuantity())
                        .orderId(stockMessage.getOrderId())
                        .reason("재고 부족 (현재 재고 : " + stock+" / 요청 수량 : " + stockMessage.getQuantity()+" )")
                        .build();
            kafkaProducer.sendFail("stock-reduce-fail",responseDto);
        }
    }


}
