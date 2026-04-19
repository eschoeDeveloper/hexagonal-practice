package io.github.eschoe.hexagonal.order.adapter.out.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderMapper {

    Optional<OrderEntity> findById(@Param("id") Long id);

    List<OrderEntity> findByUserId(@Param("userId") Long userId);

    void insertOrder(OrderEntity entity);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    List<OrderItemEntity> findItemsByOrderId(@Param("orderId") Long orderId);

    void insertItem(OrderItemEntity item);
}
