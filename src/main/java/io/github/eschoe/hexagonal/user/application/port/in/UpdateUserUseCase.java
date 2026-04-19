package io.github.eschoe.hexagonal.user.application.port.in;

import io.github.eschoe.hexagonal.user.domain.User;

public interface UpdateUserUseCase {
    User update(UpdateUserCommand command);
}
