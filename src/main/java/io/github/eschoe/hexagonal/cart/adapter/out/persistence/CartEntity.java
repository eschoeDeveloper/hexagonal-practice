package io.github.eschoe.hexagonal.cart.adapter.out.persistence;

public class CartEntity {
    private Long id;
    private Long userId;

    public CartEntity() {}

    public CartEntity(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
