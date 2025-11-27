package com.sparta.order.infrastructure.repository;

import com.sparta.order.domain.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemRepositoryAdaptor implements OrderItemRepository {

    private OrderItemJpaRepository orderItemJpaRepository;

}
