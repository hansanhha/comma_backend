package know_wave.comma.arduino_.dto.arduino;

import know_wave.comma.arduino_.dto.category.CategoryIdDto;
import know_wave.comma.arduino_.entity.Arduino;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ArduinoCreateForm {

    public ArduinoCreateForm() {
    }

    public ArduinoCreateForm(String arduinoName, int count, String description, List<CategoryIdDto> categories) {
        this.arduinoName = arduinoName;
        this.count = count;
        this.description = description;
        this.categories = categories;
    }

    @NotEmpty(message = "{Required}")
    private String arduinoName;

    @NotNull(message = "{Required}")
    @Min(value = 0, message = "{NotAcceptable.range}")
    private int count;

    @NotEmpty(message = "{Required}")
    private String description;

    @NotEmpty(message = "{Required}")
    private List<CategoryIdDto> categories;

    public Arduino toEntity() {
        return new Arduino(arduinoName, count, description);
    }

    public String getArduinoName() {
        return arduinoName;
    }

    public void setArduinoName(String arduinoName) {
        this.arduinoName = arduinoName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getCategories() {
        return categories.stream().map(CategoryIdDto::getCategoryId).toList();
    }

    public void setCategories(List<CategoryIdDto> categories) {
        this.categories = categories;
    }
}
