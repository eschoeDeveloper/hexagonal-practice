package io.github.eschoe.hexagonal.common.actuator;

import io.github.eschoe.hexagonal.common.config.AppProperties;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ShippingInfoContributor implements InfoContributor {

    private final AppProperties appProperties;

    public ShippingInfoContributor(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("shipping", Map.of(
            "defaultCarrier", appProperties.shipping().defaultCarrier()
        ));
        builder.withDetail("order", Map.of(
            "maxItemsPerOrder", appProperties.order().maxItemsPerOrder()
        ));
    }
}
