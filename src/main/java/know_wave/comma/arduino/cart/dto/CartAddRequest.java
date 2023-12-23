package know_wave.comma.arduino.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartAddRequest {

    @NotNull
    @JsonProperty("count")
    private int count;
}
