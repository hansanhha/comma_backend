package know_wave.comma.arduino.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartAddRequest {

    public static CartAddRequest create(int count) {
        return new CartAddRequest(count);
    }

    @NotNull
    @JsonProperty("count")
    private int count;
}
