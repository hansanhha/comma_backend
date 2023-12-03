package know_wave.comma.arduino_.dto.category;

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
