package io.github.eschoe.hexagonal.cart.application.port.in;

public interface ClearCartUseCase {
    void clearByUserId(Long userId);
}
