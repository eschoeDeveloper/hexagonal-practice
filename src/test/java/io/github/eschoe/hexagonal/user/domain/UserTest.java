package io.github.eschoe.hexagonal.user.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void newUserIsCreatedWithCurrentTime() {
        User user = User.newUser(new Email("a@b.com"), "Alice");
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("Alice");
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @Test
    void blankNameIsRejected() {
        assertThatThrownBy(() -> User.newUser(new Email("a@b.com"), "  "))
            .isInstanceOf(DomainException.class);
    }

    @Test
    void renameChangesName() {
        User user = User.newUser(new Email("a@b.com"), "Alice");
        user.rename("Bob");
        assertThat(user.getName()).isEqualTo("Bob");
    }
}
