package io.github.eschoe.hexagonal.order.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShipmentTest {

    @Test
    void dispatchRequiresReady() {
        Shipment shipment = Shipment.ready(1L, "Seoul");
        shipment.dispatch();
        assertThat(shipment.getStatus()).isEqualTo(ShipmentStatus.DISPATCHED);
        assertThat(shipment.getDispatchedAt()).isNotNull();
    }

    @Test
    void cannotDispatchTwice() {
        Shipment shipment = Shipment.ready(1L, "Seoul");
        shipment.dispatch();
        assertThatThrownBy(shipment::dispatch).isInstanceOf(DomainException.class);
    }

    @Test
    void deliverRequiresDispatched() {
        Shipment shipment = Shipment.ready(1L, "Seoul");
        assertThatThrownBy(shipment::deliver).isInstanceOf(DomainException.class);
        shipment.dispatch();
        shipment.deliver();
        assertThat(shipment.getStatus()).isEqualTo(ShipmentStatus.DELIVERED);
    }
}
