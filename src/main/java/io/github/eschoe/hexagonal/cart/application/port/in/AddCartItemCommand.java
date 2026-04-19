package io.github.eschoe.hexagonal.cart.application.port.in;

import java.math.BigDecimal;

public record AddCartItemCommand(
    Long userId,
    Long productId,
    String productName,
    BigDecimal unitPrice,
    int quantity
) {
}
