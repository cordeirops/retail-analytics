package dev.pedro.tam.resource;

import dev.pedro.tam.service.RetailAnalyticsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class DataHealthCheck implements HealthCheck {

    @Inject
    RetailAnalyticsService service;

    @Override
    public HealthCheckResponse call() {
        int productCount = service.listAll().size();
        if (productCount > 0) {
            return HealthCheckResponse.named("retail-data-check")
                .up()
                .withData("product-count", productCount)
                .build();
        }
        return HealthCheckResponse.named("retail-data-check")
            .down()
            .withData("reason", "No products available")
            .build();
    }
}
