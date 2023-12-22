package know_wave.comma.arduino.basket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BasketUpdateRequest {

    @JsonProperty("target_basket_id")
    private Long basketId;

    @JsonProperty("updated_count")
    private int updatedCount;
}
