package io.github.eschoe.hexagonal.order.application.service;

import io.github.eschoe.hexagonal.common.config.AppProperties;
import io.github.eschoe.hexagonal.common.exception.DomainException;
import io.github.eschoe.hexagonal.order.application.port.in.PlaceOrderCommand;
import io.github.eschoe.hexagonal.order.application.port.out.*;
import io.github.eschoe.hexagonal.order.domain.Order;
import io.github.eschoe.hexagonal.order.domain.OrderItem;
import io.github.eschoe.hexagonal.order.domain.Shipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private LoadOrderPort loadOrderPort;
    private SaveOrderPort saveOrderPort;
    private LoadShipmentPort loadShipmentPort;
    private SaveShipmentPort saveShipmentPort;
    private LoadCartForOrderPort loadCartForOrderPort;
    private ClearCartPort clearCartPort;
    private OrderService service;

    @BeforeEach
    void setUp() {
        loadOrderPort = mock(LoadOrderPort.class);
        saveOrderPort = mock(SaveOrderPort.class);
        loadShipmentPort = mock(LoadShipmentPort.class);
        saveShipmentPort = mock(SaveShipmentPort.class);
        loadCartForOrderPort = mock(LoadCartForOrderPort.class);
        clearCartPort = mock(ClearCartPort.class);
        AppProperties props = new AppProperties(new AppProperties.Order(50), new AppProperties.Shipping("TEST"));
        service = new OrderService(loadOrderPort, saveOrderPort, loadShipmentPort,
            saveShipmentPort, loadCartForOrderPort, clearCartPort, props);
    }

    @Test
    void placeFromCartPersistsOrderAndShipmentAndClearsCart() {
        when(loadCartForOrderPort.loadForOrder(1L)).thenReturn(List.of(
            new OrderItem(null, 10L, "A", new BigDecimal("1000"), 2)
        ));
        when(saveOrderPort.save(any())).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            return new Order(100L, o.getUserId(), o.getStatus(), o.getTotalAmount(), o.getItems(), o.getCreatedAt());
        });
        when(saveShipmentPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Order result = service.placeFromCart(new PlaceOrderCommand(1L, "Seoul"));

        assertThat(result.getId()).isEqualTo(100L);
        verify(saveShipmentPort).save(any(Shipment.class));
        verify(clearCartPort).clearByUserId(1L);
    }

    @Test
    void placeFailsWithEmptyCart() {
        when(loadCartForOrderPort.loadForOrder(1L)).thenReturn(List.of());
        assertThatThrownBy(() -> service.placeFromCart(new PlaceOrderCommand(1L, "Seoul")))
            .isInstanceOf(DomainException.class);
        verify(saveOrderPort, never()).save(any());
    }

    @Test
    void dispatchMarksOrderPaidAndShipmentDispatched() {
        Order order = new Order(100L, 1L, io.github.eschoe.hexagonal.order.domain.OrderStatus.PLACED,
            new BigDecimal("2000"),
            List.of(new OrderItem(1L, 10L, "A", new BigDecimal("1000"), 2)),
            LocalDateTime.now());
        Shipment shipment = Shipment.ready(100L, "Seoul");

        when(loadOrderPort.findById(100L)).thenReturn(Optional.of(order));
        when(loadShipmentPort.findByOrderId(100L)).thenReturn(Optional.of(shipment));
        when(saveOrderPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(saveShipmentPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Shipment result = service.dispatch(100L);

        assertThat(order.getStatus()).isEqualTo(io.github.eschoe.hexagonal.order.domain.OrderStatus.PAID);
        assertThat(result.getStatus()).isEqualTo(io.github.eschoe.hexagonal.order.domain.ShipmentStatus.DISPATCHED);
    }
}
