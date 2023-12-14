package know_wave.comma.arduino.component.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("categories")
    private final List<Long> categories;

    @JsonProperty("photo_files")
    private final List<MultipartFile> photoFiles;

    public static Arduino to(ArduinoCreateForm form) {
        return Arduino.builder()
                .name(form.getArduinoName())
                .count(form.getCount())
                .description(form.getDescription())
                .categories(form.getCategories().stream().map(Category::new).toList())
                .build();
    }

}
