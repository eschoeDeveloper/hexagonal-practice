package io.github.eschoe.hexagonal.cart.application.port.in;

import io.github.eschoe.hexagonal.cart.domain.Cart;

public interface GetCartUseCase {
    Cart getByUserId(Long userId);
}
