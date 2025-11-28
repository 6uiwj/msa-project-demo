package com.sparta.order.infrastructure.repository;

import com.sparta.order.domain.entity.OrderItem;
import com.sparta.order.domain.repository.OrderItemRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemRepositoryAdaptor implements OrderItemRepository {

    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemJpaRepository.save(orderItem);
    }

    @Override
    public void deleteById(UUID orderItemId) {
        orderItemJpaRepository.deleteById(orderItemId);
    }
}
