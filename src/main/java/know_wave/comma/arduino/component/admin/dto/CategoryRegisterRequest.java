package know_wave.comma.arduino.component.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CategoryRegisterRequest {

    @NotEmpty(message = "{required}")
    @JsonProperty("category_name")
    private String categoryName;
}
