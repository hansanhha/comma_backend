package know_wave.comma.arduino.component.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ArduinoUpdateForm {

    public static ArduinoUpdateForm create(String arduinoName, int count, String description, List<Long> categories) {
        return new ArduinoUpdateForm(arduinoName, count, description, categories);
    }

    public ArduinoUpdateForm(@BindParam("update_arduino_name") String updatedArduinoName,
                             @BindParam("update_count") int updatedCount,
                             @BindParam("update_description") String updatedDescription,
                             @BindParam("update_categories") List<Long> updatedCategories) {
        this.updatedArduinoName = updatedArduinoName;
        this.updatedCount = updatedCount;
        this.updatedDescription = updatedDescription;
        this.updatedCategories = updatedCategories;
    }

    @NotBlank(message = "{required}")
    private final String updatedArduinoName;

    @NotNull(message = "{required}")
    private final int updatedCount;

    @NotBlank(message = "{required}")
    private final String updatedDescription;

    @NotNull(message = "{required}")
    private final List<Long> updatedCategories;

}
