package io.github.eschoe.hexagonal.order.application.port.in;

import io.github.eschoe.hexagonal.order.domain.Order;

import java.util.List;

public interface ListUserOrdersUseCase {
    List<Order> listByUserId(Long userId);
}
