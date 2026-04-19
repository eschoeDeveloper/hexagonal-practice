package io.github.eschoe.hexagonal.order.application.port.out;

import io.github.eschoe.hexagonal.order.domain.OrderItem;

import java.util.List;

public interface LoadCartForOrderPort {
    List<OrderItem> loadForOrder(Long userId);
}
