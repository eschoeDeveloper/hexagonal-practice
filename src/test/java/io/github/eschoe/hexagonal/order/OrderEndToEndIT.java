package io.github.eschoe.hexagonal.order;

import io.github.eschoe.hexagonal.support.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderEndToEndIT extends AbstractIntegrationTest {

    private static final Pattern ID_PATTERN = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void userCanRegisterAddCartPlaceOrderAndDispatch() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        MvcResult registerResult = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email":"e2e@test.com","name":"E2E"}
                    """))
            .andExpect(status().isCreated())
            .andReturn();
        Long userId = readFirstId(registerResult);

        mvc.perform(post("/api/users/" + userId + "/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"productId":10,"productName":"Apple","unitPrice":1000,"quantity":2}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.items.length()").value(1))
            .andExpect(jsonPath("$.data.totalAmount").value(2000));

        MvcResult orderResult = mvc.perform(post("/api/users/" + userId + "/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"shippingAddress":"Seoul"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.status").value("PLACED"))
            .andReturn();
        Long orderId = readFirstId(orderResult);

        mvc.perform(get("/api/users/" + userId + "/cart"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.items.length()").value(0));

        mvc.perform(post("/api/orders/" + orderId + "/dispatch"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status").value("DISPATCHED"));

        mvc.perform(get("/api/orders/" + orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status").value("PAID"));
    }

    private Long readFirstId(MvcResult result) throws Exception {
        Matcher m = ID_PATTERN.matcher(result.getResponse().getContentAsString());
        if (!m.find()) throw new IllegalStateException("id not found in response");
        return Long.parseLong(m.group(1));
    }
}
