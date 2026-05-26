package dev.pedro.tam.model;

import java.util.Map;

public class SalesSummary {

    public String period;
    public Double totalRevenue;
    public Integer totalOrders;
    public String topCategory;
    public Map<String, Double> revenueByCategory;

    public SalesSummary() {}

    public SalesSummary(String period, Double totalRevenue, Integer totalOrders,
                        String topCategory, Map<String, Double> revenueByCategory) {
        this.period = period;
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.topCategory = topCategory;
        this.revenueByCategory = revenueByCategory;
    }
}
