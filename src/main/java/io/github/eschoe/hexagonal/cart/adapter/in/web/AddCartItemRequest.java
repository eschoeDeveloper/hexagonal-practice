package io.github.eschoe.hexagonal.cart.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AddCartItemRequest(
    @NotNull @Positive Long productId,
    @NotBlank String productName,
    @NotNull @PositiveOrZero BigDecimal unitPrice,
    @Positive int quantity
) {
}
