package com.shpp.dto;

import jakarta.validation.constraints.Positive;

import javax.validation.constraints.Digits;

public class DeliveryDTO {

    private int productId;
    private int storeId;
    @Positive
    private int productCount;

    public DeliveryDTO(int productId, int storeId, int productCount) {
        this.productId = productId;
        this.storeId = storeId;
        this.productCount = productCount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
