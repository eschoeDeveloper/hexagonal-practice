package io.github.eschoe.hexagonal.cart.application.port.in;

import io.github.eschoe.hexagonal.cart.domain.Cart;

public interface AddCartItemUseCase {
    Cart addItem(AddCartItemCommand command);
}
