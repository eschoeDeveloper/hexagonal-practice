package io.github.eschoe.hexagonal.order.application.port.out;

import io.github.eschoe.hexagonal.order.domain.Order;

public interface SaveOrderPort {
    Order save(Order order);
}
