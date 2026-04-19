package io.github.eschoe.hexagonal.user.adapter.out.persistence;

import io.github.eschoe.hexagonal.support.AbstractIntegrationTest;
import io.github.eschoe.hexagonal.user.domain.Email;
import io.github.eschoe.hexagonal.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserPersistenceAdapterIT extends AbstractIntegrationTest {

    @Autowired
    private UserPersistenceAdapter adapter;

    @Test
    void saveAndFindById() {
        User saved = adapter.save(User.newUser(new Email("it-user@test.com"), "IT"));

        assertThat(saved.getId()).isNotNull();

        Optional<User> found = adapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail().value()).isEqualTo("it-user@test.com");
        assertThat(found.get().getName()).isEqualTo("IT");
    }

    @Test
    void findByEmailReturnsEmptyWhenMissing() {
        assertThat(adapter.findByEmail(new Email("missing@test.com"))).isEmpty();
    }
}
