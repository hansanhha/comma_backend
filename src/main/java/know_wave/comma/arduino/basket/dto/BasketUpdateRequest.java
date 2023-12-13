package know_wave.comma.arduino.basket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BasketUpdateRequest {

    @JsonProperty("target_basket_id")
    private final Long basketId;

    @JsonProperty("updated_count")
    private final int updatedCount;
}
