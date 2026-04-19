package io.github.eschoe.hexagonal.cart.adapter.in.web;

import jakarta.validation.constraints.Positive;

public record ChangeQuantityRequest(@Positive int quantity) {
}
