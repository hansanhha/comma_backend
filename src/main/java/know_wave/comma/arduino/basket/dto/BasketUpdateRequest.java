package know_wave.comma.arduino.basket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BasketUpdateRequest {

    @NotNull
    @JsonProperty("update_basket_id")
    private Long basketId;

    @NotNull
    @JsonProperty("update_count")
    private int updatedCount;
}
