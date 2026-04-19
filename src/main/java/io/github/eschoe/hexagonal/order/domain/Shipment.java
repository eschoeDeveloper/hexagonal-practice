package io.github.eschoe.hexagonal.order.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;

import java.time.LocalDateTime;

public class Shipment {

    private final Long id;
    private final Long orderId;
    private final String address;
    private ShipmentStatus status;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveredAt;

    public Shipment(Long id, Long orderId, String address, ShipmentStatus status,
                    LocalDateTime dispatchedAt, LocalDateTime deliveredAt) {
        if (orderId == null) throw new DomainException("orderId required");
        if (address == null || address.isBlank()) throw new DomainException("address required");
        this.id = id;
        this.orderId = orderId;
        this.address = address;
        this.status = status;
        this.dispatchedAt = dispatchedAt;
        this.deliveredAt = deliveredAt;
    }

    public static Shipment ready(Long orderId, String address) {
        return new Shipment(null, orderId, address, ShipmentStatus.READY, null, null);
    }

    public void dispatch() {
        if (status != ShipmentStatus.READY) {
            throw new DomainException("Shipment must be READY to dispatch. current=" + status);
        }
        this.status = ShipmentStatus.DISPATCHED;
        this.dispatchedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (status != ShipmentStatus.DISPATCHED) {
            throw new DomainException("Shipment must be DISPATCHED to deliver. current=" + status);
        }
        this.status = ShipmentStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public String getAddress() { return address; }
    public ShipmentStatus getStatus() { return status; }
    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
}
