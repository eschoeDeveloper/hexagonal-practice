package io.github.eschoe.hexagonal.user.application.port.out;

import io.github.eschoe.hexagonal.user.domain.Email;
import io.github.eschoe.hexagonal.user.domain.User;

import java.util.Optional;

public interface LoadUserPort {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(Email email);
}
