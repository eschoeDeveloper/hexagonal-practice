package io.github.eschoe.hexagonal.cart.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;

import java.math.BigDecimal;

public class CartItem {

    private final Long id;
    private final Long productId;
    private final String productName;
    private final BigDecimal unitPrice;
    private int quantity;

    public CartItem(Long id, Long productId, String productName, BigDecimal unitPrice, int quantity) {
        if (productId == null) throw new DomainException("productId required");
        if (productName == null || productName.isBlank()) throw new DomainException("productName required");
        if (unitPrice == null || unitPrice.signum() < 0) throw new DomainException("unitPrice must be >= 0");
        if (quantity <= 0) throw new DomainException("quantity must be > 0");
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public void increaseQuantity(int delta) {
        if (delta <= 0) throw new DomainException("delta must be > 0");
        this.quantity += delta;
    }

    public void changeQuantity(int newQuantity) {
        if (newQuantity <= 0) throw new DomainException("quantity must be > 0");
        this.quantity = newQuantity;
    }

    public BigDecimal subtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
}
