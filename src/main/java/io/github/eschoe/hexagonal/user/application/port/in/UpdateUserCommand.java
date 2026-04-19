package io.github.eschoe.hexagonal.user.application.port.in;

public record UpdateUserCommand(Long id, String name) {
}
