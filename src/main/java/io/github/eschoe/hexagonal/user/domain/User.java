package io.github.eschoe.hexagonal.user.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;

import java.time.LocalDateTime;

public class User {

    private final Long id;
    private final Email email;
    private String name;
    private final LocalDateTime createdAt;

    public User(Long id, Email email, String name, LocalDateTime createdAt) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Name must not be blank");
        }
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static User newUser(Email email, String name) {
        return new User(null, email, name, LocalDateTime.now());
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new DomainException("Name must not be blank");
        }
        this.name = newName;
    }

    public Long getId() { return id; }
    public Email getEmail() { return email; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
