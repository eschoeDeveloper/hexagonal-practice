package io.github.eschoe.hexagonal.order.application.port.in;

import io.github.eschoe.hexagonal.order.domain.Order;
import io.github.eschoe.hexagonal.order.domain.Shipment;

public interface GetOrderUseCase {
    Order getById(Long id);
    Shipment getShipmentByOrderId(Long orderId);
}
