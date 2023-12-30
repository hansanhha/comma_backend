package know_wave.comma.arduino.component.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ArduinoCreateForm {

    @JsonProperty("arduino_name")
    private final String arduinoName;

    @JsonProperty("count")
    private final int count;

    @JsonProperty("description")
    private final String description;

    @NotNull(message = "{required}")
    @JsonProperty("categories")
    private final List<Long> categories;

    @JsonProperty("photo_files")
    private final List<MultipartFile> photoFiles;

}
