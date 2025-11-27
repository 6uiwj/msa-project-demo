package com.sparta.order.domain.repository;

import com.sparta.order.domain.entity.OrderItem;

public interface OrderItemRepository {
    OrderItem save(OrderItem orderItem);
}
