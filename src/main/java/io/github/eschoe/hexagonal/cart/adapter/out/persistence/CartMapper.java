package io.github.eschoe.hexagonal.cart.adapter.out.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CartMapper {

    Optional<CartEntity> findByUserId(@Param("userId") Long userId);

    void insertCart(CartEntity cart);

    List<CartItemEntity> findItemsByCartId(@Param("cartId") Long cartId);

    void insertItem(CartItemEntity item);

    void deleteItemsByCartId(@Param("cartId") Long cartId);
}
