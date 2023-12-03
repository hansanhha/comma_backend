package know_wave.comma.order_.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderMoreRequest {

    @NotNull(message = "{Required}")
    @Min(value = 1, message = "{NotAcceptable.range}")
    private Long arduinoId;
    @NotNull(message = "{Required}")
    @Min(value = 1, message = "{NotAcceptable.range}")
    @Max(value = 10, message = "{NotAcceptable.range}")
    private int orderCount;


    public Long getArduinoId() {
        return arduinoId;
    }

    public void setArduinoId(Long arduinoId) {
        this.arduinoId = arduinoId;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
