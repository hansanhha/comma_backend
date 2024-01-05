package know_wave.comma.arduino.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartUpdateRequest {

    public static CartUpdateRequest create(int updatedCount) {
        return new CartUpdateRequest(updatedCount);
    }

    @NotNull
    @JsonProperty("update_count")
    private int updatedCount;
}
