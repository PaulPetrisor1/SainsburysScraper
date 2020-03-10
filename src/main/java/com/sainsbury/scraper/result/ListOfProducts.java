package com.sainsbury.scraper.result;

import java.util.List;

public class ListOfProducts {

    private List<Product> results;

    private double total;

    public ListOfProducts(List<Product> productList) {
        this.results = productList;
        this.total = productList.stream().mapToDouble(Product::getUnitPrice).sum();
    }
}
