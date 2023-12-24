package know_wave.comma.arduino.component.dto;

import know_wave.comma.arduino.component.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriesResponse {

    public static CategoriesResponse to(List<Category> categories) {
        return new CategoriesResponse(categories.stream()
                .map(CategoryResponse::to).toList());
    }

    private final List<CategoryResponse> categories;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class CategoryResponse {
        private final Long categoryId;
        private final String categoryName;

        public static CategoryResponse to(Category category) {
            return new CategoryResponse(category.getId(), category.getName());
        }
    }
}
