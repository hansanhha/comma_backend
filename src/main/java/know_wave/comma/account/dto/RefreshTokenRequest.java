package know_wave.comma.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {

    @JsonProperty("refresh_token")
    @NotEmpty(message = "{Required}")
    private String refreshToken;
}
