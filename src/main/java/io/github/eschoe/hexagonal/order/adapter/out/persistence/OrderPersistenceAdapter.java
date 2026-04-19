package io.github.eschoe.hexagonal.order.adapter.out.persistence;

import io.github.eschoe.hexagonal.order.application.port.out.LoadOrderPort;
import io.github.eschoe.hexagonal.order.application.port.out.LoadShipmentPort;
import io.github.eschoe.hexagonal.order.application.port.out.SaveOrderPort;
import io.github.eschoe.hexagonal.order.application.port.out.SaveShipmentPort;
import io.github.eschoe.hexagonal.order.domain.Order;
import io.github.eschoe.hexagonal.order.domain.OrderItem;
import io.github.eschoe.hexagonal.order.domain.OrderStatus;
import io.github.eschoe.hexagonal.order.domain.Shipment;
import io.github.eschoe.hexagonal.order.domain.ShipmentStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderPersistenceAdapter implements LoadOrderPort, SaveOrderPort, LoadShipmentPort, SaveShipmentPort {

    private final OrderMapper orderMapper;
    private final ShipmentMapper shipmentMapper;

    public OrderPersistenceAdapter(OrderMapper orderMapper, ShipmentMapper shipmentMapper) {
        this.orderMapper = orderMapper;
        this.shipmentMapper = shipmentMapper;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderMapper.findById(id).map(this::toDomain);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderMapper.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    private Order toDomain(OrderEntity entity) {
        List<OrderItem> items = orderMapper.findItemsByOrderId(entity.getId()).stream()
            .map(e -> new OrderItem(e.getId(), e.getProductId(), e.getProductName(), e.getUnitPrice(), e.getQuantity()))
            .toList();
        return new Order(
            entity.getId(),
            entity.getUserId(),
            OrderStatus.valueOf(entity.getStatus()),
            entity.getTotalAmount(),
            items,
            entity.getCreatedAt()
        );
    }

    @Override
    public Order save(Order order) {
        if (order.getId() == null) {
            OrderEntity entity = new OrderEntity(
                null, order.getUserId(), order.getStatus().name(),
                order.getTotalAmount(), order.getCreatedAt()
            );
            orderMapper.insertOrder(entity);
            for (OrderItem item : order.getItems()) {
                OrderItemEntity itemEntity = new OrderItemEntity(
                    null, entity.getId(), item.getProductId(), item.getProductName(),
                    item.getUnitPrice(), item.getQuantity()
                );
                orderMapper.insertItem(itemEntity);
            }
            return findById(entity.getId()).orElseThrow();
        } else {
            orderMapper.updateStatus(order.getId(), order.getStatus().name());
            return findById(order.getId()).orElseThrow();
        }
    }

    @Override
    public Optional<Shipment> findByOrderId(Long orderId) {
        return shipmentMapper.findByOrderId(orderId).map(e -> new Shipment(
            e.getId(), e.getOrderId(), e.getAddress(),
            ShipmentStatus.valueOf(e.getStatus()),
            e.getDispatchedAt(), e.getDeliveredAt()
        ));
    }

    @Override
    public Shipment save(Shipment shipment) {
        ShipmentEntity entity = new ShipmentEntity(
            shipment.getId(), shipment.getOrderId(), shipment.getAddress(),
            shipment.getStatus().name(), shipment.getDispatchedAt(), shipment.getDeliveredAt()
        );
        if (entity.getId() == null) {
            shipmentMapper.insert(entity);
        } else {
            shipmentMapper.update(entity);
        }
        return findByOrderId(shipment.getOrderId()).orElseThrow();
    }
}
