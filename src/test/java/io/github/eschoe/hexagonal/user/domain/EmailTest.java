package io.github.eschoe.hexagonal.user.domain;

import io.github.eschoe.hexagonal.common.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void validEmailIsAccepted() {
        Email email = new Email("foo@bar.com");
        assertThat(email.value()).isEqualTo("foo@bar.com");
    }

    @Test
    void invalidEmailIsRejected() {
        assertThatThrownBy(() -> new Email("not-an-email")).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new Email(null)).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new Email("a@b")).isInstanceOf(DomainException.class);
    }
}
