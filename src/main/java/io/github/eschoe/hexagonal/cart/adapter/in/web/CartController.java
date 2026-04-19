package io.github.eschoe.hexagonal.cart.adapter.in.web;

import io.github.eschoe.hexagonal.cart.application.port.in.*;
import io.github.eschoe.hexagonal.common.web.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/cart")
public class CartController {

    private final AddCartItemUseCase addCartItemUseCase;
    private final RemoveCartItemUseCase removeCartItemUseCase;
    private final ChangeCartItemQuantityUseCase changeCartItemQuantityUseCase;
    private final GetCartUseCase getCartUseCase;

    public CartController(AddCartItemUseCase addCartItemUseCase,
                          RemoveCartItemUseCase removeCartItemUseCase,
                          ChangeCartItemQuantityUseCase changeCartItemQuantityUseCase,
                          GetCartUseCase getCartUseCase) {
        this.addCartItemUseCase = addCartItemUseCase;
        this.removeCartItemUseCase = removeCartItemUseCase;
        this.changeCartItemQuantityUseCase = changeCartItemQuantityUseCase;
        this.getCartUseCase = getCartUseCase;
    }

    @GetMapping
    public ApiResponse<CartResponse> get(@PathVariable Long userId) {
        return ApiResponse.ok(CartResponse.from(getCartUseCase.getByUserId(userId)));
    }

    @PostMapping("/items")
    public ApiResponse<CartResponse> addItem(@PathVariable Long userId, @Valid @RequestBody AddCartItemRequest request) {
        var cart = addCartItemUseCase.addItem(new AddCartItemCommand(
            userId, request.productId(), request.productName(), request.unitPrice(), request.quantity()
        ));
        return ApiResponse.ok(CartResponse.from(cart));
    }

    @PatchMapping("/items/{productId}")
    public ApiResponse<CartResponse> changeQuantity(@PathVariable Long userId,
                                                    @PathVariable Long productId,
                                                    @Valid @RequestBody ChangeQuantityRequest request) {
        return ApiResponse.ok(CartResponse.from(
            changeCartItemQuantityUseCase.changeQuantity(userId, productId, request.quantity())
        ));
    }

    @DeleteMapping("/items/{productId}")
    public ApiResponse<CartResponse> removeItem(@PathVariable Long userId, @PathVariable Long productId) {
        return ApiResponse.ok(CartResponse.from(removeCartItemUseCase.removeItem(userId, productId)));
    }
}
