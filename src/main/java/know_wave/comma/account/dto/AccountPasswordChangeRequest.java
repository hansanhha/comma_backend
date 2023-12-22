package know_wave.comma.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
public class AccountPasswordChangeRequest {

    @JsonProperty("password")
    @NotEmpty(message = "{Required}")
    @Length(min = 8, max = 255, message = "{Length}")
    private String password;

}
