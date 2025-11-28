package com.sparta.order.application;

import com.sparta.order.application.service.ProductClient;
import com.sparta.order.domain.entity.Order;
import com.sparta.order.domain.entity.OrderItem;
import com.sparta.order.domain.repository.OrderItemRepository;
import com.sparta.order.domain.repository.OrderRepository;
import com.sparta.order.infrastructure.dto.OrderCreateSuccessResponse;
import com.sparta.order.infrastructure.dto.OrderDeleteMessage;
import com.sparta.order.infrastructure.dto.OrderMessage;
import com.sparta.order.infrastructure.dto.ProductDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;

    public OrderCreateSuccessResponse createOrder(OrderMessage orderMessage) {

        ProductDto productDto = new ProductDto();

        //feinnClient 통신 (order -> product)
        try {
            log.info("[feign: order->product] productId : {}", orderMessage.getProductId());
            productDto = productClient.getProduct(orderMessage.getProductId()).getBody();
            log.info("Product 가져오기 성공: {}", productDto.getProductId());

        } catch (Exception e) {
            log.info("[feign: order -> product] feignClient 통신 실패");
            throw new RuntimeException("feignClient getProduct 가져오기 실패");
        }
        int price = productDto.getPrice();


        Order order = Order.create(orderMessage.getQuantity()*price);
        orderRepository.save(order);

        OrderItem orderItem = OrderItem.create(orderMessage.getProductId(),orderMessage.getQuantity(),price,order);
        orderItemRepository.save(orderItem);


        OrderCreateSuccessResponse orderCreateSuccessResponse = new OrderCreateSuccessResponse();
        orderCreateSuccessResponse.setOrderId(order.getOrderId());
        orderCreateSuccessResponse.setQuantity(orderItem.getQuantity());
        orderCreateSuccessResponse.setProductId(orderItem.getProductId());
        orderCreateSuccessResponse.setSagaId(orderMessage.getSagaId());

        return orderCreateSuccessResponse;
    }

    public void deleteOrder(OrderDeleteMessage orderDeleteMessage) {

        UUID orderId = orderDeleteMessage.getOrderId();
        orderRepository.deleteById(orderId);
    }
}
