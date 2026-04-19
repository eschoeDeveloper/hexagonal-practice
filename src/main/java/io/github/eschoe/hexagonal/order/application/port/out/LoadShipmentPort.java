package io.github.eschoe.hexagonal.order.application.port.out;

import io.github.eschoe.hexagonal.order.domain.Shipment;

import java.util.Optional;

public interface LoadShipmentPort {
    Optional<Shipment> findByOrderId(Long orderId);
}
