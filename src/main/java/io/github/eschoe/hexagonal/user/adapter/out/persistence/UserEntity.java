package io.github.eschoe.hexagonal.user.adapter.out.persistence;

import io.github.eschoe.hexagonal.user.domain.Email;
import io.github.eschoe.hexagonal.user.domain.User;

import java.time.LocalDateTime;

public class UserEntity {

    private Long id;
    private String email;
    private String name;
    private LocalDateTime createdAt;

    public UserEntity() {}

    public UserEntity(Long id, String email, String name, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static UserEntity from(User user) {
        return new UserEntity(user.getId(), user.getEmail().value(), user.getName(), user.getCreatedAt());
    }

    public User toDomain() {
        return new User(id, new Email(email), name, createdAt);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
