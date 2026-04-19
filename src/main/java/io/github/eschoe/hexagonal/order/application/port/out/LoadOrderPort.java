package io.github.eschoe.hexagonal.order.application.port.out;

import io.github.eschoe.hexagonal.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface LoadOrderPort {
    Optional<Order> findById(Long id);
    List<Order> findByUserId(Long userId);
}
