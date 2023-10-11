package com.know_wave.comma.comma_backend.arduino.dto.category;

import jakarta.validation.constraints.NotEmpty;

public class CategoryNameDto {

    public CategoryNameDto() {
    }

    public CategoryNameDto(String categoryName) {
        this.categoryName = categoryName;
    }

    @NotEmpty(message = "{Required}")
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
