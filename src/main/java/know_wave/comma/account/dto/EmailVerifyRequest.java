package know_wave.comma.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import static know_wave.comma.common.entity.regexPattern.emailRegex;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerifyRequest {

    public static EmailVerifyRequest create(String email, String code) {
        return EmailVerifyRequest.builder()
                .email(email)
                .code(code)
                .build();
    }

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
