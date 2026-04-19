package io.github.eschoe.hexagonal.order.application.service;

import io.github.eschoe.hexagonal.common.config.AppProperties;
import io.github.eschoe.hexagonal.common.exception.DomainException;
import io.github.eschoe.hexagonal.common.exception.NotFoundException;
import io.github.eschoe.hexagonal.order.application.port.in.CancelOrderUseCase;
import io.github.eschoe.hexagonal.order.application.port.in.DeliverShipmentUseCase;
import io.github.eschoe.hexagonal.order.application.port.in.DispatchShipmentUseCase;
import io.github.eschoe.hexagonal.order.application.port.in.GetOrderUseCase;
import io.github.eschoe.hexagonal.order.application.port.in.ListUserOrdersUseCase;
import io.github.eschoe.hexagonal.order.application.port.in.PlaceOrderCommand;
import io.github.eschoe.hexagonal.order.application.port.in.PlaceOrderUseCase;
import io.github.eschoe.hexagonal.order.application.port.out.*;
import io.github.eschoe.hexagonal.order.domain.Order;
import io.github.eschoe.hexagonal.order.domain.OrderItem;
import io.github.eschoe.hexagonal.order.domain.Shipment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService implements PlaceOrderUseCase, DispatchShipmentUseCase, GetOrderUseCase,
        CancelOrderUseCase, DeliverShipmentUseCase, ListUserOrdersUseCase {

    private final LoadOrderPort loadOrderPort;
    private final SaveOrderPort saveOrderPort;
    private final LoadShipmentPort loadShipmentPort;
    private final SaveShipmentPort saveShipmentPort;
    private final LoadCartForOrderPort loadCartForOrderPort;
    private final ClearCartPort clearCartPort;
    private final AppProperties appProperties;

    public OrderService(LoadOrderPort loadOrderPort,
                        SaveOrderPort saveOrderPort,
                        LoadShipmentPort loadShipmentPort,
                        SaveShipmentPort saveShipmentPort,
                        LoadCartForOrderPort loadCartForOrderPort,
                        ClearCartPort clearCartPort,
                        AppProperties appProperties) {
        this.loadOrderPort = loadOrderPort;
        this.saveOrderPort = saveOrderPort;
        this.loadShipmentPort = loadShipmentPort;
        this.saveShipmentPort = saveShipmentPort;
        this.loadCartForOrderPort = loadCartForOrderPort;
        this.clearCartPort = clearCartPort;
        this.appProperties = appProperties;
    }

    @Override
    public Order placeFromCart(PlaceOrderCommand command) {
        List<OrderItem> items = loadCartForOrderPort.loadForOrder(command.userId());
        if (items.isEmpty()) {
            throw new DomainException("Cannot place order with empty cart");
        }
        int limit = appProperties.order().maxItemsPerOrder();
        if (items.size() > limit) {
            throw new DomainException("Too many items in order: " + items.size() + " > " + limit);
        }
        Order order = Order.place(command.userId(), items);
        Order saved = saveOrderPort.save(order);
        saveShipmentPort.save(Shipment.ready(saved.getId(), command.shippingAddress()));
        clearCartPort.clearByUserId(command.userId());
        return saved;
    }

    @Override
    public Shipment dispatch(Long orderId) {
        Order order = loadOrderPort.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        order.markPaid();
        saveOrderPort.save(order);

        Shipment shipment = loadShipmentPort.findByOrderId(orderId)
            .orElseThrow(() -> new NotFoundException("Shipment not found for order: " + orderId));
        shipment.dispatch();
        return saveShipmentPort.save(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getById(Long id) {
        return loadOrderPort.findById(id)
            .orElseThrow(() -> new NotFoundException("Order not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Shipment getShipmentByOrderId(Long orderId) {
        return loadShipmentPort.findByOrderId(orderId)
            .orElseThrow(() -> new NotFoundException("Shipment not found for order: " + orderId));
    }

    @Override
    public Order cancel(Long orderId) {
        Order order = loadOrderPort.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        order.cancel();
        return saveOrderPort.save(order);
    }

    @Override
    public Shipment deliver(Long orderId) {
        Order order = loadOrderPort.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        order.complete();
        saveOrderPort.save(order);

        Shipment shipment = loadShipmentPort.findByOrderId(orderId)
            .orElseThrow(() -> new NotFoundException("Shipment not found for order: " + orderId));
        shipment.deliver();
        return saveShipmentPort.save(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> listByUserId(Long userId) {
        return loadOrderPort.findByUserId(userId);
    }
}
