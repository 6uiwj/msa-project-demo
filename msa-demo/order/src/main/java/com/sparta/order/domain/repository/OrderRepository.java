package com.sparta.order.domain.repository;

import com.sparta.order.domain.entity.Order;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    void deleteById(UUID orderId);
}
