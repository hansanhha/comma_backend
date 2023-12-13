package know_wave.comma.arduino.basket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BasketAddRequest {

    @JsonProperty("target_arduino_id")
    private final Long arduinoId;

    @JsonProperty("count")
    private final int count;
}
