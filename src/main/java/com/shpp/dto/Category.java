package com.shpp.dto;

import jakarta.validation.constraints.NotBlank;

public class Category {
    @NotBlank
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
