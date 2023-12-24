package know_wave.comma.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;

import static know_wave.comma.common.entity.regexPattern.emailRegex;

@Getter
public class EmailVerifyRequest {

    @JsonProperty("email")
    @NotEmpty(message = "{Required}")
    @Email(regexp = emailRegex,
            message = "{Email.email}")
    private String email;

    @JsonProperty("code")
    @NotEmpty(message = "{Required}")
    @Range(min = 111111, max = 999999, message = "{Range.code}")
    private String code;

}
