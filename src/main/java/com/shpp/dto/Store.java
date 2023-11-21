package com.shpp.dto;

import jakarta.validation.constraints.NotBlank;

public class Store {
    @NotBlank
    private String location;

    public Store(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
