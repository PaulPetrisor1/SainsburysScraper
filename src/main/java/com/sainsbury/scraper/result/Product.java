package com.sainsbury.scraper.result;

import java.util.Objects;

public final class Product {

    private final String title;
    private final Integer kcalPer100G;
    private final double unitPrice;
    private String description;

    public Product(String title, Integer kcalPer100G, double unitPrice, String description) {
        this.title = title;
        this.kcalPer100G = kcalPer100G > 0 ? kcalPer100G : null;
        this.unitPrice = unitPrice;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public int getkCalPer100g() {
        return kcalPer100G;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", kcalPer100G='" + kcalPer100G + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.getUnitPrice(), getUnitPrice()) == 0 &&
                Objects.equals(getTitle(), product.getTitle()) &&
                Objects.equals(kcalPer100G, product.kcalPer100G) &&
                Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), kcalPer100G, getUnitPrice(), description);
    }
}
