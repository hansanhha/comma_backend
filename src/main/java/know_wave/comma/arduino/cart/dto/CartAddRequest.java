package know_wave.comma.arduino.cart.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CartAddRequest {

    public static CartAddRequest create(int count) {
        return new CartAddRequest(count);
    }

    @JsonCreator
    private CartAddRequest(int count) {
        this.count = count;
    }

    @NotNull
    @JsonProperty("count")
    private int count;
}
