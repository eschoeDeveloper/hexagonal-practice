package io.github.eschoe.hexagonal.order.application.port.in;

import io.github.eschoe.hexagonal.order.domain.Shipment;

public interface DeliverShipmentUseCase {
    Shipment deliver(Long orderId);
}
