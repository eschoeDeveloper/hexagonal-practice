package io.github.eschoe.hexagonal.order.adapter.in.web;

import io.github.eschoe.hexagonal.order.domain.Order;
import io.github.eschoe.hexagonal.order.domain.OrderItem;
import io.github.eschoe.hexagonal.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long id,
    Long userId,
    OrderStatus status,
    BigDecimal totalAmount,
    List<Item> items,
    LocalDateTime createdAt
) {
    public record Item(Long productId, String productName, BigDecimal unitPrice, int quantity) {
        static Item from(OrderItem i) {
            return new Item(i.getProductId(), i.getProductName(), i.getUnitPrice(), i.getQuantity());
        }
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getUserId(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getItems().stream().map(Item::from).toList(),
            order.getCreatedAt()
        );
    }
}
