package io.github.eschoe.hexagonal.user.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(@NotBlank String name) {
}
