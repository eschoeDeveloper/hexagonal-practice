package io.github.eschoe.hexagonal.order.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private final Long id;
    private final Long userId;
    private OrderStatus status;
    private final BigDecimal totalAmount;
    private final List<OrderItem> items;
    private final LocalDateTime createdAt;

    public Order(Long id, Long userId, OrderStatus status, BigDecimal totalAmount,
                 List<OrderItem> items, LocalDateTime createdAt) {
        if (userId == null) throw new DomainException("userId required");
        if (items == null || items.isEmpty()) throw new DomainException("Order must have at least one item");
        if (totalAmount == null || totalAmount.signum() < 0) throw new DomainException("totalAmount invalid");
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.items = new ArrayList<>(items);
        this.createdAt = createdAt;
    }

    public static Order place(Long userId, List<OrderItem> items) {
        BigDecimal total = items.stream().map(OrderItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Order(null, userId, OrderStatus.PLACED, total, items, LocalDateTime.now());
    }

    public void cancel() {
        if (status == OrderStatus.COMPLETED) {
            throw new DomainException("Cannot cancel a completed order");
        }
        this.status = OrderStatus.CANCELED;
    }

    public void markPaid() {
        if (status != OrderStatus.PLACED) {
            throw new DomainException("Only PLACED orders can be paid. current=" + status);
        }
        this.status = OrderStatus.PAID;
    }

    public void complete() {
        if (status != OrderStatus.PAID) {
            throw new DomainException("Only PAID orders can be completed. current=" + status);
        }
        this.status = OrderStatus.COMPLETED;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
