package io.github.eschoe.hexagonal.order.adapter.out.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface ShipmentMapper {

    Optional<ShipmentEntity> findByOrderId(@Param("orderId") Long orderId);

    void insert(ShipmentEntity entity);

    int update(ShipmentEntity entity);
}
