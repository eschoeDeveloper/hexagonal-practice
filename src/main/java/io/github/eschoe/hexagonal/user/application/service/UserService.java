package io.github.eschoe.hexagonal.user.application.service;

import io.github.eschoe.hexagonal.common.exception.DomainException;
import io.github.eschoe.hexagonal.common.exception.NotFoundException;
import io.github.eschoe.hexagonal.user.application.port.in.*;
import io.github.eschoe.hexagonal.user.application.port.out.DeleteUserPort;
import io.github.eschoe.hexagonal.user.application.port.out.LoadUserPort;
import io.github.eschoe.hexagonal.user.application.port.out.SaveUserPort;
import io.github.eschoe.hexagonal.user.domain.Email;
import io.github.eschoe.hexagonal.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements RegisterUserUseCase, GetUserUseCase, UpdateUserUseCase, DeleteUserUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final DeleteUserPort deleteUserPort;

    public UserService(LoadUserPort loadUserPort, SaveUserPort saveUserPort, DeleteUserPort deleteUserPort) {
        this.loadUserPort = loadUserPort;
        this.saveUserPort = saveUserPort;
        this.deleteUserPort = deleteUserPort;
    }

    @Override
    public User register(RegisterUserCommand command) {
        Email email = new Email(command.email());
        loadUserPort.findByEmail(email).ifPresent(u -> {
            throw new DomainException("Email already registered: " + email.value());
        });
        User user = User.newUser(email, command.name());
        return saveUserPort.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return loadUserPort.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Override
    public User update(UpdateUserCommand command) {
        User user = loadUserPort.findById(command.id())
            .orElseThrow(() -> new NotFoundException("User not found: " + command.id()));
        user.rename(command.name());
        return saveUserPort.save(user);
    }

    @Override
    public void delete(Long id) {
        loadUserPort.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found: " + id));
        deleteUserPort.deleteById(id);
    }
}
