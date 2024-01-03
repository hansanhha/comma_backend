package know_wave.comma.arduino.component.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
public class CategoryCreateRequest {

    public static CategoryCreateRequest create(String name) {
        return new CategoryCreateRequest(name);
    }

    @JsonCreator
    public CategoryCreateRequest(@JsonProperty("category_name") String categoryName) {
        this.categoryName = categoryName;
    }

    @NotEmpty(message = "{required}")
    private final String categoryName;
}
