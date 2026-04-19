package io.github.eschoe.hexagonal.user.application.service;

import io.github.eschoe.hexagonal.common.exception.DomainException;
import io.github.eschoe.hexagonal.common.exception.NotFoundException;
import io.github.eschoe.hexagonal.user.application.port.in.RegisterUserCommand;
import io.github.eschoe.hexagonal.user.application.port.in.UpdateUserCommand;
import io.github.eschoe.hexagonal.user.application.port.out.DeleteUserPort;
import io.github.eschoe.hexagonal.user.application.port.out.LoadUserPort;
import io.github.eschoe.hexagonal.user.application.port.out.SaveUserPort;
import io.github.eschoe.hexagonal.user.domain.Email;
import io.github.eschoe.hexagonal.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private LoadUserPort loadUserPort;
    private SaveUserPort saveUserPort;
    private DeleteUserPort deleteUserPort;
    private UserService service;

    @BeforeEach
    void setUp() {
        loadUserPort = mock(LoadUserPort.class);
        saveUserPort = mock(SaveUserPort.class);
        deleteUserPort = mock(DeleteUserPort.class);
        service = new UserService(loadUserPort, saveUserPort, deleteUserPort);
    }

    @Test
    void registerPersistsNewUser() {
        when(loadUserPort.findByEmail(any())).thenReturn(Optional.empty());
        when(saveUserPort.save(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            return new User(1L, u.getEmail(), u.getName(), u.getCreatedAt());
        });

        User result = service.register(new RegisterUserCommand("a@b.com", "Alice"));

        assertThat(result.getId()).isEqualTo(1L);
        verify(saveUserPort).save(any());
    }

    @Test
    void registerFailsWhenEmailExists() {
        when(loadUserPort.findByEmail(any())).thenReturn(Optional.of(
            new User(1L, new Email("a@b.com"), "Existing", LocalDateTime.now())
        ));

        assertThatThrownBy(() -> service.register(new RegisterUserCommand("a@b.com", "Alice")))
            .isInstanceOf(DomainException.class);
        verify(saveUserPort, never()).save(any());
    }

    @Test
    void getByIdThrowsWhenNotFound() {
        when(loadUserPort.findById(42L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(42L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateRenamesPersistedUser() {
        User existing = new User(1L, new Email("a@b.com"), "Alice", LocalDateTime.now());
        when(loadUserPort.findById(1L)).thenReturn(Optional.of(existing));
        when(saveUserPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = service.update(new UpdateUserCommand(1L, "Bob"));

        assertThat(result.getName()).isEqualTo("Bob");
        verify(saveUserPort).save(existing);
    }

    @Test
    void deleteCallsPortWhenUserExists() {
        when(loadUserPort.findById(1L)).thenReturn(Optional.of(
            new User(1L, new Email("a@b.com"), "Alice", LocalDateTime.now())
        ));
        service.delete(1L);
        verify(deleteUserPort).deleteById(1L);
    }

    @Test
    void deleteThrowsWhenUserMissing() {
        when(loadUserPort.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(NotFoundException.class);
        verify(deleteUserPort, never()).deleteById(anyLong());
    }
}
