package com.sparta.order.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="p_order_item")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderItemId;

    private UUID productId;

    private int quantity;

    private int singlePrice;

    @ManyToOne
    private Order order;

    protected OrderItem( UUID productId, int quantity, int singlePrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.singlePrice = singlePrice;
    }

    public static  OrderItem create(UUID productId, int quantity, int singlePrice) {
        return new OrderItem(productId, quantity, singlePrice);
    }

    protected void setOrder(Order order) {
        this.order = order;
    }
}
