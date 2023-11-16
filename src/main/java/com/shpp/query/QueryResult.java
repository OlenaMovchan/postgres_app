package com.shpp.query;

public class QueryResult {
    private final String categoryName;
    private final String storeLocation;
    private final int totalProducts;

    public QueryResult(String categoryName, String storeLocation, int totalProducts) {
        this.categoryName = categoryName;
        this.storeLocation = storeLocation;
        this.totalProducts = totalProducts;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public int getTotalProducts() {
        return totalProducts;
    }
}