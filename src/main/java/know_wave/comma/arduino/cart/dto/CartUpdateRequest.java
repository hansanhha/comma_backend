package know_wave.comma.arduino.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartUpdateRequest {

    @NotNull
    @JsonProperty("update_count")
    private int updatedCount;
}
