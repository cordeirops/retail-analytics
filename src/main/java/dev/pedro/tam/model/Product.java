package dev.pedro.tam.model;

public class Product {

    public Long id;
    public String name;
    public String category;
    public Double price;
    public Integer stockQuantity;

    public Product() {}

    public Product(Long id, String name, String category, Double price, Integer stockQuantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
