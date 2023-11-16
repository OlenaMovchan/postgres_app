package com.shpp.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
public class ProductDTO {
    @NotBlank
    private String productName;
    @Min(1)
    @Max(1000)
    private int categoryId;

    public ProductDTO(String productName, int categoryId) {
        this.productName = productName;
        this.categoryId = categoryId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
