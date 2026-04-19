package io.github.eschoe.hexagonal.order.adapter.in.web;

import io.github.eschoe.hexagonal.order.domain.Shipment;
import io.github.eschoe.hexagonal.order.domain.ShipmentStatus;

import java.time.LocalDateTime;

public record ShipmentResponse(
    Long id,
    Long orderId,
    String address,
    ShipmentStatus status,
    LocalDateTime dispatchedAt,
    LocalDateTime deliveredAt
) {
    public static ShipmentResponse from(Shipment s) {
        return new ShipmentResponse(
            s.getId(), s.getOrderId(), s.getAddress(), s.getStatus(), s.getDispatchedAt(), s.getDeliveredAt()
        );
    }
}
