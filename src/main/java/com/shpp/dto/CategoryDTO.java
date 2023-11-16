package com.shpp.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {
    @NotBlank
    private String categoryName;

    public CategoryDTO(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
