package io.github.eschoe.hexagonal.order.application.port.in;

public record PlaceOrderCommand(Long userId, String shippingAddress) {
}
