package io.github.eschoe.hexagonal.user.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new DomainException("Invalid email: " + value);
        }
    }
}
