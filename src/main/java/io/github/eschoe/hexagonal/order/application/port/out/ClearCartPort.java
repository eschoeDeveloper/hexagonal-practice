package io.github.eschoe.hexagonal.order.application.port.out;

public interface ClearCartPort {
    void clearByUserId(Long userId);
}
