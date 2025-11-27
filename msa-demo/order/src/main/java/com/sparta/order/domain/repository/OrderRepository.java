package com.sparta.order.domain.repository;

import com.sparta.order.domain.entity.Order;

public interface OrderRepository {
    Order save(Order order);
}
