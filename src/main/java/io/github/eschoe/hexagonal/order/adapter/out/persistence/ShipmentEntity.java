package io.github.eschoe.hexagonal.order.adapter.out.persistence;

import java.time.LocalDateTime;

public class ShipmentEntity {
    private Long id;
    private Long orderId;
    private String address;
    private String status;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveredAt;

    public ShipmentEntity() {}

    public ShipmentEntity(Long id, Long orderId, String address, String status,
                          LocalDateTime dispatchedAt, LocalDateTime deliveredAt) {
        this.id = id;
        this.orderId = orderId;
        this.address = address;
        this.status = status;
        this.dispatchedAt = dispatchedAt;
        this.deliveredAt = deliveredAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
}
