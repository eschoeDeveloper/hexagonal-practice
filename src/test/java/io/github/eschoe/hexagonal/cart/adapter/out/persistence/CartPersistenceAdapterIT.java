package io.github.eschoe.hexagonal.cart.adapter.out.persistence;

import io.github.eschoe.hexagonal.cart.domain.Cart;
import io.github.eschoe.hexagonal.support.AbstractIntegrationTest;
import io.github.eschoe.hexagonal.user.adapter.out.persistence.UserPersistenceAdapter;
import io.github.eschoe.hexagonal.user.domain.Email;
import io.github.eschoe.hexagonal.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CartPersistenceAdapterIT extends AbstractIntegrationTest {

    @Autowired
    private CartPersistenceAdapter cartAdapter;
    @Autowired
    private UserPersistenceAdapter userAdapter;

    @Test
    void saveNewCartAndReload() {
        User user = userAdapter.save(User.newUser(new Email("cart-it@test.com"), "CartIT"));

        Cart cart = Cart.empty(user.getId());
        cart.addItem(100L, "Apple", new BigDecimal("1000"), 2);
        cart.addItem(200L, "Banana", new BigDecimal("500"), 3);

        Cart saved = cartAdapter.save(cart);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getItems()).hasSize(2);
        assertThat(saved.totalAmount()).isEqualByComparingTo("3500");

        Cart reloaded = cartAdapter.findByUserId(user.getId()).orElseThrow();
        assertThat(reloaded.getItems()).hasSize(2);
    }
}
