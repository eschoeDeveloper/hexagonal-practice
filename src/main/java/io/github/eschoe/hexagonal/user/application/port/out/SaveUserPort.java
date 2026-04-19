package io.github.eschoe.hexagonal.user.application.port.out;

import io.github.eschoe.hexagonal.user.domain.User;

public interface SaveUserPort {
    User save(User user);
}
