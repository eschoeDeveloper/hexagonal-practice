package io.github.eschoe.hexagonal.user.adapter.out.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    Optional<UserEntity> findById(@Param("id") Long id);

    Optional<UserEntity> findByEmail(@Param("email") String email);

    void insert(UserEntity entity);

    int update(UserEntity entity);

    int deleteById(@Param("id") Long id);
}
