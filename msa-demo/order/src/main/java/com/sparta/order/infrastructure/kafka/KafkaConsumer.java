package com.sparta.order.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.order.application.service.ProductClient;
import com.sparta.order.domain.entity.Order;
import com.sparta.order.domain.entity.OrderItem;
import com.sparta.order.domain.repository.OrderItemRepository;
import com.sparta.order.domain.repository.OrderRepository;
import com.sparta.order.infrastructure.dto.OrderCreateSuccessResponse;
import com.sparta.order.infrastructure.dto.OrderMessage;
import com.sparta.order.infrastructure.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final KafkaProducer kafkaProducer;

    public KafkaConsumer(ProductClient productClient, OrderRepository orderRepository,
        OrderItemRepository orderItemRepository, KafkaProducer kafkaProducer) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @KafkaListener(topics = "order-create-request")
    public void createOrder(String kafkaMessage) {
        log.info("Kafka Message : {}", kafkaMessage);

        OrderMessage orderMessage = null;

        // Jackson의 ObjectMapper (JSON문자열을 -> Java 객체 변환)
        ObjectMapper mapper = new ObjectMapper();

        try {
            orderMessage = mapper.readValue(kafkaMessage, OrderMessage.class);
            if(orderMessage == null) {
                throw new RuntimeException("카프카 메시지 내용이 비어있음");
            }
            log.info("Kafka 메시지 구독 성공");
        } catch (JsonProcessingException e) {

            // JSON 파싱 실패 시 예외
            e.printStackTrace();
        }

        ProductDto productDto = new ProductDto();
        //feignClient로 product에 있는 상품인가 검증하고 가격 가져오기
        try {
            log.info("productId : {}", orderMessage.getProductId());
            productDto = productClient.getProduct(orderMessage.getProductId()).getBody();
            log.info("Product 가져오기 성공: {}", productDto.getProductId());

        } catch (Exception e) {
            log.info("[order -> product] feignClient 통신 실패");
            throw new RuntimeException("feignClient getProduct 가져오기 실패");
        }

        OrderItem orderItem = OrderItem.create(orderMessage.getProductId(), orderMessage.getQuantity(), productDto.getPrice());
        System.out.println("주문상품 엔티티에 담는거 성공");
        orderItemRepository.save(orderItem);
        System.out.println("주문상품 save 성공");
        Order order = Order.create(orderMessage.getQuantity()* productDto.getPrice());
        System.out.println("주문 엔티티 담는거 성공");
        orderRepository.save(order);
        System.out.println("주문 성공");

        OrderCreateSuccessResponse orderCreateSuccessResponse = new OrderCreateSuccessResponse();
        orderCreateSuccessResponse.setOrderId(order.getOrderId());
        orderCreateSuccessResponse.setQuantity(orderItem.getQuantity());
        orderCreateSuccessResponse.setProductId(orderItem.getProductId());

        kafkaProducer.send("order-success-create", orderCreateSuccessResponse);
    }
}
