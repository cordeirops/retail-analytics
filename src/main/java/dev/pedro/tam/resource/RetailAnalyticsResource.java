package dev.pedro.tam.resource;

import dev.pedro.tam.model.Product;
import dev.pedro.tam.model.SalesSummary;
import dev.pedro.tam.service.RetailAnalyticsService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
public class RetailAnalyticsResource {

    @Inject
    RetailAnalyticsService service;

    @Inject
    MeterRegistry registry;

    // GET /api/v1/products
    @GET
    @Path("/products")
    public List<Product> listProducts(@QueryParam("category") String category) {
        registry.counter("retail.products.listed").increment();
        if (category != null && !category.isBlank()) {
            return service.listByCategory(category);
        }
        return service.listAll();
    }

    // GET /api/v1/products/{id}
    @GET
    @Path("/products/{id}")
    public Response getProduct(@PathParam("id") Long id) {
        registry.counter("retail.products.fetched").increment();
        Product product = service.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Product not found", "id", id))
                .build();
        }
        return Response.ok(product).build();
    }

    // GET /api/v1/analytics/summary
    @GET
    @Path("/analytics/summary")
    public SalesSummary getMonthlySummary() {
        registry.counter("retail.analytics.summary.requested").increment();
        return service.getMonthlySummary();
    }

    // GET /api/v1/health/info — endpoint simples para demo no console OpenShift
    @GET
    @Path("/info")
    public Map<String, String> info() {
        return Map.of(
            "app",         "retail-analytics",
            "version",     "1.0.0",
            "framework",   "Quarkus 3.15.1",
            "description", "Retail data analysis API - Red Hat TAM Demo"
        );
    }
}
