package io.github.eschoe.hexagonal.cart.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Cart {

    private final Long id;
    private final Long userId;
    private final List<CartItem> items;

    public Cart(Long id, Long userId, List<CartItem> items) {
        if (userId == null) throw new DomainException("userId required");
        this.id = id;
        this.userId = userId;
        this.items = new ArrayList<>(items == null ? List.of() : items);
    }

    public static Cart empty(Long userId) {
        return new Cart(null, userId, new ArrayList<>());
    }

    public void addItem(Long productId, String productName, BigDecimal unitPrice, int quantity) {
        Optional<CartItem> existing = findByProductId(productId);
        if (existing.isPresent()) {
            existing.get().increaseQuantity(quantity);
        } else {
            items.add(new CartItem(null, productId, productName, unitPrice, quantity));
        }
    }

    public void removeItem(Long productId) {
        items.removeIf(i -> i.getProductId().equals(productId));
    }

    public void changeQuantity(Long productId, int newQuantity) {
        CartItem item = findByProductId(productId)
            .orElseThrow(() -> new DomainException("Item not in cart: " + productId));
        item.changeQuantity(newQuantity);
    }

    public BigDecimal totalAmount() {
        return items.stream().map(CartItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    private Optional<CartItem> findByProductId(Long productId) {
        return items.stream().filter(i -> i.getProductId().equals(productId)).findFirst();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public List<CartItem> getItems() { return Collections.unmodifiableList(items); }
}
