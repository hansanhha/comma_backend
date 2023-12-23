package know_wave.comma.arduino.basket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BasketAddRequest {

    @NotNull
    @JsonProperty("add_arduino_id")
    private Long arduinoId;

    @NotNull
    @JsonProperty("count")
    private int count;
}
