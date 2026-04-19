package io.github.eschoe.hexagonal.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Order order, Shipping shipping) {

    public AppProperties {
        if (order == null) order = new Order(50);
        if (shipping == null) shipping = new Shipping("HEX-LOGISTICS");
    }

    public record Order(int maxItemsPerOrder) {}

    public record Shipping(String defaultCarrier) {}
}
