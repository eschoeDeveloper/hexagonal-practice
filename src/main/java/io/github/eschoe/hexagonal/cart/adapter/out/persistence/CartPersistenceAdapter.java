package io.github.eschoe.hexagonal.cart.adapter.out.persistence;

import io.github.eschoe.hexagonal.cart.application.port.out.LoadCartPort;
import io.github.eschoe.hexagonal.cart.application.port.out.SaveCartPort;
import io.github.eschoe.hexagonal.cart.domain.Cart;
import io.github.eschoe.hexagonal.cart.domain.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CartPersistenceAdapter implements LoadCartPort, SaveCartPort {

    private final CartMapper cartMapper;

    public CartPersistenceAdapter(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return cartMapper.findByUserId(userId).map(entity -> {
            List<CartItem> items = cartMapper.findItemsByCartId(entity.getId()).stream()
                .map(e -> new CartItem(e.getId(), e.getProductId(), e.getProductName(), e.getUnitPrice(), e.getQuantity()))
                .toList();
            return new Cart(entity.getId(), entity.getUserId(), items);
        });
    }

    @Override
    public Cart save(Cart cart) {
        Long cartId = cart.getId();
        if (cartId == null) {
            CartEntity entity = new CartEntity(null, cart.getUserId());
            cartMapper.insertCart(entity);
            cartId = entity.getId();
        }
        cartMapper.deleteItemsByCartId(cartId);
        for (CartItem item : cart.getItems()) {
            CartItemEntity itemEntity = new CartItemEntity(
                null, cartId, item.getProductId(), item.getProductName(),
                item.getUnitPrice(), item.getQuantity()
            );
            cartMapper.insertItem(itemEntity);
        }
        final Long finalCartId = cartId;
        List<CartItem> reloaded = cartMapper.findItemsByCartId(finalCartId).stream()
            .map(e -> new CartItem(e.getId(), e.getProductId(), e.getProductName(), e.getUnitPrice(), e.getQuantity()))
            .toList();
        return new Cart(finalCartId, cart.getUserId(), reloaded);
    }
}
