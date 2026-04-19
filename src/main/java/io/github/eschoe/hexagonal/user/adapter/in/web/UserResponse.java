package io.github.eschoe.hexagonal.user.adapter.in.web;

import io.github.eschoe.hexagonal.user.domain.User;

import java.time.LocalDateTime;

public record UserResponse(Long id, String email, String name, LocalDateTime createdAt) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail().value(), user.getName(), user.getCreatedAt());
    }
}
