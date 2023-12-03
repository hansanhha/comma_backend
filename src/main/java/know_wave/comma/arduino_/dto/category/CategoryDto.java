package know_wave.comma.arduino_.dto.category;

import know_wave.comma.arduino_.entity.Category;

public class CategoryDto {

    public CategoryDto() {
    }

    public CategoryDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public static CategoryDto of(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    private Long categoryId;

    private String categoryName;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
