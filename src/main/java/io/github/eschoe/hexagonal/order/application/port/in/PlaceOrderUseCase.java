package io.github.eschoe.hexagonal.order.application.port.in;

import io.github.eschoe.hexagonal.order.domain.Order;

public interface PlaceOrderUseCase {
    Order placeFromCart(PlaceOrderCommand command);
}
