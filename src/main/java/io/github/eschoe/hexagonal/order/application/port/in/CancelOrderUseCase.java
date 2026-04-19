package io.github.eschoe.hexagonal.order.application.port.in;

import io.github.eschoe.hexagonal.order.domain.Order;

public interface CancelOrderUseCase {
    Order cancel(Long orderId);
}
