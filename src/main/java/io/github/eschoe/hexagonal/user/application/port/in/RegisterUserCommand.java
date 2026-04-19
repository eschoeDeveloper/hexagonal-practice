package io.github.eschoe.hexagonal.user.application.port.in;

public record RegisterUserCommand(String email, String name) {
}
