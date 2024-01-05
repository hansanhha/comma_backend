package know_wave.comma.arduino.component.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@RequiredArgsConstructor(onConstructor_ = {@JsonCreator})
public class CategoryCreateRequest {

    public static CategoryCreateRequest create(String name) {
        return new CategoryCreateRequest(name);
    }

    @JsonProperty("category_name")
    @NotEmpty(message = "{required}")
    private final String categoryName;
}
