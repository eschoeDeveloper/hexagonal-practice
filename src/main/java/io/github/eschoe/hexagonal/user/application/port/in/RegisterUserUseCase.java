package io.github.eschoe.hexagonal.user.application.port.in;

import io.github.eschoe.hexagonal.user.domain.User;

public interface RegisterUserUseCase {
    User register(RegisterUserCommand command);
}
