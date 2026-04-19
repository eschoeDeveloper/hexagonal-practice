package io.github.eschoe.hexagonal.user.adapter.in.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequest(
    @NotBlank @Email String email,
    @NotBlank String name
) {
}
