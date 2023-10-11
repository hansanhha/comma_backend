package com.know_wave.comma.comma_backend.arduino.dto.category;

public class CategoryIdDto {

    public CategoryIdDto() {
    }

    public CategoryIdDto(Long categoryId) {
        this.categoryId = categoryId;
    }

    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
