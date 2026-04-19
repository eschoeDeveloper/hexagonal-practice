package io.github.eschoe.hexagonal.cart.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {

    @Test
    void addItemMergesSameProduct() {
        Cart cart = Cart.empty(1L);
        cart.addItem(10L, "Apple", new BigDecimal("1000"), 2);
        cart.addItem(10L, "Apple", new BigDecimal("1000"), 3);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    void totalAmountSumsSubtotals() {
        Cart cart = Cart.empty(1L);
        cart.addItem(1L, "A", new BigDecimal("1000"), 2);
        cart.addItem(2L, "B", new BigDecimal("500"), 3);

        assertThat(cart.totalAmount()).isEqualByComparingTo("3500");
    }

    @Test
    void removeItemEliminatesProduct() {
        Cart cart = Cart.empty(1L);
        cart.addItem(1L, "A", new BigDecimal("1000"), 2);
        cart.removeItem(1L);

        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    void changeQuantityFailsForUnknownProduct() {
        Cart cart = Cart.empty(1L);
        assertThatThrownBy(() -> cart.changeQuantity(99L, 1)).isInstanceOf(DomainException.class);
    }

    @Test
    void zeroQuantityIsRejected() {
        Cart cart = Cart.empty(1L);
        assertThatThrownBy(() -> cart.addItem(1L, "A", new BigDecimal("1000"), 0))
            .isInstanceOf(DomainException.class);
    }
}
