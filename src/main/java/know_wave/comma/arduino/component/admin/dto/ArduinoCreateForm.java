package know_wave.comma.arduino.component.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.bind.annotation.BindParam;

import java.util.List;

@Getter
public class ArduinoCreateForm {

    public static ArduinoCreateForm create(String arduinoName, int count, String description, List<Long> categories) {
        return new ArduinoCreateForm(arduinoName, count, description, categories);
    }

    public ArduinoCreateForm(@BindParam("arduino_name") String arduinoName,
                             @BindParam("count") int count,
                             @BindParam("description") String description,
                             @BindParam("categories") List<Long> categories) {
        this.arduinoName = arduinoName;
        this.count = count;
        this.description = description;
        this.categories = categories;
    }

    @NotBlank(message = "{required}")
    private final String arduinoName;

    @NotNull(message = "{required}")
    private final int count;

    @NotBlank(message = "{required}")
    private final String description;

    @NotNull(message = "{required}")
    private final List<Long> categories;

}
