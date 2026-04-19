package io.github.eschoe.hexagonal.cart.application.port.out;

import io.github.eschoe.hexagonal.cart.domain.Cart;

import java.util.Optional;

public interface LoadCartPort {
    Optional<Cart> findByUserId(Long userId);
}
