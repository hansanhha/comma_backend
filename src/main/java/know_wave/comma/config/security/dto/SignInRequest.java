package know_wave.comma.config.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {

    public static SignInRequest create(String accountId, String password) {
        return new SignInRequest(accountId, password);
    }

    @JsonProperty("account_id")
    @NotEmpty(message = "{Required}")
    @Length(min = 4, max = 255, message = "{Length.min}")
    private String accountId;

    @JsonProperty("password")
    @NotEmpty(message = "{Required}")
    @Length(min = 8, max = 255, message = "{Length.min}")
    private String password;
}
