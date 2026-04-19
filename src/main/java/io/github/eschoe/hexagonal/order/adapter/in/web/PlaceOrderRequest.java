package io.github.eschoe.hexagonal.order.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderRequest(
    @NotBlank String shippingAddress
) {
}
