package io.github.eschoe.hexagonal.cart.application.port.out;

import io.github.eschoe.hexagonal.cart.domain.Cart;

public interface SaveCartPort {
    Cart save(Cart cart);
}
