package io.github.eschoe.hexagonal.cart.application.port.in;

import io.github.eschoe.hexagonal.cart.domain.Cart;

public interface ChangeCartItemQuantityUseCase {
    Cart changeQuantity(Long userId, Long productId, int quantity);
}
