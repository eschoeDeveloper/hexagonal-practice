package io.github.eschoe.hexagonal.cart.adapter.in.web;

import io.github.eschoe.hexagonal.cart.domain.Cart;
import io.github.eschoe.hexagonal.cart.domain.CartItem;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(Long id, Long userId, List<Item> items, BigDecimal totalAmount) {

    public record Item(Long id, Long productId, String productName, BigDecimal unitPrice, int quantity, BigDecimal subtotal) {
        static Item from(CartItem i) {
            return new Item(i.getId(), i.getProductId(), i.getProductName(), i.getUnitPrice(), i.getQuantity(), i.subtotal());
        }
    }

    public static CartResponse from(Cart cart) {
        return new CartResponse(
            cart.getId(),
            cart.getUserId(),
            cart.getItems().stream().map(Item::from).toList(),
            cart.totalAmount()
        );
    }
}
