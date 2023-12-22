package know_wave.comma.arduino.basket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BasketAddRequest {

    @JsonProperty("target_arduino_id")
    private Long arduinoId;

    @JsonProperty("count")
    private int count;
}
