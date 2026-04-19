package io.github.eschoe.hexagonal.cart.application.service;

import io.github.eschoe.hexagonal.cart.application.port.in.AddCartItemCommand;
import io.github.eschoe.hexagonal.cart.application.port.out.LoadCartPort;
import io.github.eschoe.hexagonal.cart.application.port.out.SaveCartPort;
import io.github.eschoe.hexagonal.cart.domain.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private LoadCartPort loadCartPort;
    private SaveCartPort saveCartPort;
    private CartService service;

    @BeforeEach
    void setUp() {
        loadCartPort = mock(LoadCartPort.class);
        saveCartPort = mock(SaveCartPort.class);
        service = new CartService(loadCartPort, saveCartPort);
    }

    @Test
    void addItemCreatesCartIfMissing() {
        when(loadCartPort.findByUserId(1L)).thenReturn(Optional.empty());
        when(saveCartPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Cart cart = service.addItem(new AddCartItemCommand(1L, 10L, "Apple", new BigDecimal("1000"), 2));

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getUserId()).isEqualTo(1L);
        verify(saveCartPort).save(any());
    }

    @Test
    void clearEmptiesExistingCart() {
        Cart existing = Cart.empty(1L);
        existing.addItem(10L, "Apple", new BigDecimal("1000"), 2);
        when(loadCartPort.findByUserId(1L)).thenReturn(Optional.of(existing));
        when(saveCartPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.clearByUserId(1L);

        assertThat(existing.isEmpty()).isTrue();
        verify(saveCartPort).save(existing);
    }

    @Test
    void clearWithoutCartIsNoop() {
        when(loadCartPort.findByUserId(1L)).thenReturn(Optional.empty());
        service.clearByUserId(1L);
        verify(saveCartPort, never()).save(any());
    }
}
