package com.shpp.dto;

import jakarta.validation.constraints.NotBlank;

public class StoreDTO {
    @NotBlank
    private String location;

    public StoreDTO(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
