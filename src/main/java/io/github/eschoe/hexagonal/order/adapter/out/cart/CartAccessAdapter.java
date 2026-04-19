package io.github.eschoe.hexagonal.order.adapter.out.cart;

import io.github.eschoe.hexagonal.cart.application.port.in.ClearCartUseCase;
import io.github.eschoe.hexagonal.cart.application.port.in.GetCartUseCase;
import io.github.eschoe.hexagonal.cart.domain.Cart;
import io.github.eschoe.hexagonal.order.application.port.out.ClearCartPort;
import io.github.eschoe.hexagonal.order.application.port.out.LoadCartForOrderPort;
import io.github.eschoe.hexagonal.order.domain.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartAccessAdapter implements LoadCartForOrderPort, ClearCartPort {

    private final GetCartUseCase getCartUseCase;
    private final ClearCartUseCase clearCartUseCase;

    public CartAccessAdapter(GetCartUseCase getCartUseCase, ClearCartUseCase clearCartUseCase) {
        this.getCartUseCase = getCartUseCase;
        this.clearCartUseCase = clearCartUseCase;
    }

    @Override
    public List<OrderItem> loadForOrder(Long userId) {
        Cart cart = getCartUseCase.getByUserId(userId);
        return cart.getItems().stream()
            .map(i -> new OrderItem(null, i.getProductId(), i.getProductName(), i.getUnitPrice(), i.getQuantity()))
            .toList();
    }

    @Override
    public void clearByUserId(Long userId) {
        clearCartUseCase.clearByUserId(userId);
    }
}
