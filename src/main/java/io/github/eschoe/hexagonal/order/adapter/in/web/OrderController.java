package io.github.eschoe.hexagonal.order.adapter.in.web;

import io.github.eschoe.hexagonal.common.web.ApiResponse;
import io.github.eschoe.hexagonal.order.application.port.in.*;
import io.github.eschoe.hexagonal.order.domain.Order;
import io.github.eschoe.hexagonal.order.domain.Shipment;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;
    private final DispatchShipmentUseCase dispatchShipmentUseCase;
    private final DeliverShipmentUseCase deliverShipmentUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListUserOrdersUseCase listUserOrdersUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase,
                           DispatchShipmentUseCase dispatchShipmentUseCase,
                           DeliverShipmentUseCase deliverShipmentUseCase,
                           CancelOrderUseCase cancelOrderUseCase,
                           GetOrderUseCase getOrderUseCase,
                           ListUserOrdersUseCase listUserOrdersUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
        this.dispatchShipmentUseCase = dispatchShipmentUseCase;
        this.deliverShipmentUseCase = deliverShipmentUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.listUserOrdersUseCase = listUserOrdersUseCase;
    }

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> place(
        @PathVariable Long userId,
        @Valid @RequestBody PlaceOrderRequest request
    ) {
        Order order = placeOrderUseCase.placeFromCart(new PlaceOrderCommand(userId, request.shippingAddress()));
        return ResponseEntity.status(201).body(ApiResponse.ok(OrderResponse.from(order)));
    }

    @GetMapping("/users/{userId}/orders")
    public ApiResponse<List<OrderResponse>> listByUser(@PathVariable Long userId) {
        List<OrderResponse> list = listUserOrdersUseCase.listByUserId(userId).stream()
            .map(OrderResponse::from).toList();
        return ApiResponse.ok(list);
    }

    @GetMapping("/orders/{id}")
    public ApiResponse<OrderResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(OrderResponse.from(getOrderUseCase.getById(id)));
    }

    @PostMapping("/orders/{id}/cancel")
    public ApiResponse<OrderResponse> cancel(@PathVariable Long id) {
        return ApiResponse.ok(OrderResponse.from(cancelOrderUseCase.cancel(id)));
    }

    @PostMapping("/orders/{id}/dispatch")
    public ApiResponse<ShipmentResponse> dispatch(@PathVariable Long id) {
        Shipment shipment = dispatchShipmentUseCase.dispatch(id);
        return ApiResponse.ok(ShipmentResponse.from(shipment));
    }

    @PostMapping("/orders/{id}/deliver")
    public ApiResponse<ShipmentResponse> deliver(@PathVariable Long id) {
        return ApiResponse.ok(ShipmentResponse.from(deliverShipmentUseCase.deliver(id)));
    }

    @GetMapping("/orders/{id}/shipment")
    public ApiResponse<ShipmentResponse> getShipment(@PathVariable Long id) {
        return ApiResponse.ok(ShipmentResponse.from(getOrderUseCase.getShipmentByOrderId(id)));
    }
}
