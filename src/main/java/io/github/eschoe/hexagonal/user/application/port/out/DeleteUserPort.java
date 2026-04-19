package io.github.eschoe.hexagonal.user.application.port.out;

public interface DeleteUserPort {
    void deleteById(Long id);
}
