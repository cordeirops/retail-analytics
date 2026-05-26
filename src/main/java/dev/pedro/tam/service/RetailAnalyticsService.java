package dev.pedro.tam.service;

import dev.pedro.tam.model.Product;
import dev.pedro.tam.model.SalesSummary;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RetailAnalyticsService {

    // Catálogo de produtos simulado — em produção viria de um datasource
    private final List<Product> products = List.of(
        new Product(1L, "Notebook Pro 15", "Electronics", 4599.99, 42),
        new Product(2L, "Wireless Mouse",  "Electronics", 129.90,  215),
        new Product(3L, "Office Desk",     "Furniture",   1299.00, 18),
        new Product(4L, "Ergonomic Chair", "Furniture",   2199.00, 9),
        new Product(5L, "USB-C Hub",       "Electronics", 189.90,  88),
        new Product(6L, "Standing Lamp",   "Furniture",   349.00,  33)
    );

    public List<Product> listAll() {
        return products;
    }

    public List<Product> listByCategory(String category) {
        return products.stream()
            .filter(p -> p.category.equalsIgnoreCase(category))
            .toList();
    }

    public Product findById(Long id) {
        return products.stream()
            .filter(p -> p.id.equals(id))
            .findFirst()
            .orElse(null);
    }

    public SalesSummary getMonthlySummary() {
        double electronics = products.stream()
            .filter(p -> p.category.equals("Electronics"))
            .mapToDouble(p -> p.price * (100 - p.stockQuantity))
            .sum();

        double furniture = products.stream()
            .filter(p -> p.category.equals("Furniture"))
            .mapToDouble(p -> p.price * (50 - p.stockQuantity))
            .sum();

        Map<String, Double> byCategory = Map.of(
            "Electronics", Math.abs(electronics),
            "Furniture",   Math.abs(furniture)
        );

        String top = Math.abs(electronics) >= Math.abs(furniture) ? "Electronics" : "Furniture";
        double total = Math.abs(electronics) + Math.abs(furniture);

        return new SalesSummary("current-month", total, 143, top, byCategory);
    }
}
