package com.sparta.order.infrastructure.repository;

import com.sparta.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdaptor implements OrderRepository {

    private OrderJpaRepository orderJpaRepository;
}
