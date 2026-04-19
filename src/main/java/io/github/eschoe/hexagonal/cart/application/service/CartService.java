package io.github.eschoe.hexagonal.cart.application.service;

import io.github.eschoe.hexagonal.cart.application.port.in.AddCartItemCommand;
import io.github.eschoe.hexagonal.cart.application.port.in.AddCartItemUseCase;
import io.github.eschoe.hexagonal.cart.application.port.in.ChangeCartItemQuantityUseCase;
import io.github.eschoe.hexagonal.cart.application.port.in.ClearCartUseCase;
import io.github.eschoe.hexagonal.cart.application.port.in.GetCartUseCase;
import io.github.eschoe.hexagonal.cart.application.port.in.RemoveCartItemUseCase;
import io.github.eschoe.hexagonal.common.exception.NotFoundException;
import io.github.eschoe.hexagonal.cart.application.port.out.LoadCartPort;
import io.github.eschoe.hexagonal.cart.application.port.out.SaveCartPort;
import io.github.eschoe.hexagonal.cart.domain.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService implements AddCartItemUseCase, RemoveCartItemUseCase, GetCartUseCase,
        ClearCartUseCase, ChangeCartItemQuantityUseCase {

    private final LoadCartPort loadCartPort;
    private final SaveCartPort saveCartPort;

    public CartService(LoadCartPort loadCartPort, SaveCartPort saveCartPort) {
        this.loadCartPort = loadCartPort;
        this.saveCartPort = saveCartPort;
    }

    @Override
    public Cart addItem(AddCartItemCommand command) {
        Cart cart = loadCartPort.findByUserId(command.userId())
            .orElseGet(() -> Cart.empty(command.userId()));
        cart.addItem(command.productId(), command.productName(), command.unitPrice(), command.quantity());
        return saveCartPort.save(cart);
    }

    @Override
    public Cart removeItem(Long userId, Long productId) {
        Cart cart = loadCartPort.findByUserId(userId)
            .orElseGet(() -> Cart.empty(userId));
        cart.removeItem(productId);
        return saveCartPort.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getByUserId(Long userId) {
        return loadCartPort.findByUserId(userId)
            .orElseGet(() -> Cart.empty(userId));
    }

    @Override
    public Cart changeQuantity(Long userId, Long productId, int quantity) {
        Cart cart = loadCartPort.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Cart not found for user: " + userId));
        cart.changeQuantity(productId, quantity);
        return saveCartPort.save(cart);
    }

    @Override
    public void clearByUserId(Long userId) {
        loadCartPort.findByUserId(userId).ifPresent(cart -> {
            cart.clear();
            saveCartPort.save(cart);
        });
    }
}
