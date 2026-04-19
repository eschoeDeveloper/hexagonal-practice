package io.github.eschoe.hexagonal.cart.adapter.out.persistence;

import java.math.BigDecimal;

public class CartItemEntity {
    private Long id;
    private Long cartId;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;

    public CartItemEntity() {}

    public CartItemEntity(Long id, Long cartId, Long productId, String productName, BigDecimal unitPrice, int quantity) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
