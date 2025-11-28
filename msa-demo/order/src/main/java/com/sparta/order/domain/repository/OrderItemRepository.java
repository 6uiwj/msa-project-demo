package com.sparta.order.domain.repository;

import com.sparta.order.domain.entity.OrderItem;
import java.util.UUID;

public interface OrderItemRepository {
    OrderItem save(OrderItem orderItem);
    void deleteById(UUID orderItemId);
}
