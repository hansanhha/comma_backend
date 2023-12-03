package know_wave.comma.arduino_.dto.arduino;

import know_wave.comma.arduino_.dto.category.CategoryIdDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ArduinoUpdateRequest {

    @NotEmpty(message = "{Required}")
    private String modifiedArduinoName;

    @NotNull(message = "{Required}")
    @Min(value = 0, message = "{NotAcceptable.range}")
    private int modifiedArduinoCount;

    @NotNull(message = "{Required}")
    @Min(value = 0, message = "{NotAcceptable.range}")
    private int modifiedArduinoOriginalCount;

    @NotEmpty(message = "{Required}")
    private String modifiedArduinoDescription;

    @NotEmpty(message = "{Required}")
    private List<CategoryIdDto> modifiedArduinoCategories;



    public String getModifiedArduinoName() {
        return modifiedArduinoName;
    }

    public void setModifiedArduinoName(String modifiedArduinoName) {
        this.modifiedArduinoName = modifiedArduinoName;
    }

    public int getModifiedArduinoCount() {
        return modifiedArduinoCount;
    }

    public void setModifiedArduinoCount(int modifiedArduinoCount) {
        this.modifiedArduinoCount = modifiedArduinoCount;
    }

    public int getModifiedArduinoOriginalCount() {
        return modifiedArduinoOriginalCount;
    }

    public void setModifiedArduinoOriginalCount(int modifiedArduinoOriginalCount) {
        this.modifiedArduinoOriginalCount = modifiedArduinoOriginalCount;
    }

    public String getModifiedArduinoDescription() {
        return modifiedArduinoDescription;
    }

    public void setModifiedArduinoDescription(String modifiedArduinoDescription) {
        this.modifiedArduinoDescription = modifiedArduinoDescription;
    }

    public List<Long> getModifiedArduinoCategories() {
        return modifiedArduinoCategories.stream().map(CategoryIdDto::getCategoryId).toList();
    }

    public void setModifiedArduinoCategories(List<CategoryIdDto> modifiedArduinoCategories) {
        this.modifiedArduinoCategories = modifiedArduinoCategories;
    }
}
