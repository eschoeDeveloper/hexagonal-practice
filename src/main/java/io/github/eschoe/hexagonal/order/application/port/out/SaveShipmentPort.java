package io.github.eschoe.hexagonal.order.application.port.out;

import io.github.eschoe.hexagonal.order.domain.Shipment;

public interface SaveShipmentPort {
    Shipment save(Shipment shipment);
}
