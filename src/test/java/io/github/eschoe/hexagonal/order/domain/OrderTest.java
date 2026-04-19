package io.github.eschoe.hexagonal.order.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private OrderItem item() {
        return new OrderItem(null, 1L, "A", new BigDecimal("1000"), 2);
    }

    @Test
    void placeComputesTotal() {
        Order order = Order.place(1L, List.of(item()));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);
        assertThat(order.getTotalAmount()).isEqualByComparingTo("2000");
    }

    @Test
    void emptyItemsRejected() {
        assertThatThrownBy(() -> Order.place(1L, List.of())).isInstanceOf(DomainException.class);
    }

    @Test
    void statusTransitionPlacedPaidCompleted() {
        Order order = Order.place(1L, List.of(item()));
        order.markPaid();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        order.complete();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    void cannotCompleteBeforePaid() {
        Order order = Order.place(1L, List.of(item()));
        assertThatThrownBy(order::complete).isInstanceOf(DomainException.class);
    }

    @Test
    void cannotCancelCompleted() {
        Order order = Order.place(1L, List.of(item()));
        order.markPaid();
        order.complete();
        assertThatThrownBy(order::cancel).isInstanceOf(DomainException.class);
    }

    @Test
    void cannotPayTwice() {
        Order order = Order.place(1L, List.of(item()));
        order.markPaid();
        assertThatThrownBy(order::markPaid).isInstanceOf(DomainException.class);
    }
}
