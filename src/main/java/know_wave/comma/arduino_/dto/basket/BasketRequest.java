package know_wave.comma.arduino_.dto.basket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BasketRequest {

    @NotNull(message = "{Required}")
    @Min(value = 1, message = "{NotAcceptable.range}")
    private Long arduinoId;

    @NotNull(message = "{Required}")
    @Min(value = 1, message = "{NotAcceptable.range}")
    @Max(value = 10, message = "{NotAcceptable.range}")
    private int containedCount;

    public Long getArduinoId() {
        return arduinoId;
    }

    public void setArduinoId(Long arduinoId) {
        this.arduinoId = arduinoId;
    }


    public int getContainedCount() {
        return containedCount;
    }

    public void setContainedCount(int containedCount) {
        this.containedCount = containedCount;
    }
}
