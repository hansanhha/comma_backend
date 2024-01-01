package know_wave.comma.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import static know_wave.comma.common.entity.regexPattern.emailRegex;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmailSendRequest {

    public static EmailSendRequest create(String email) {
        return new EmailSendRequest(email);
    }

    @JsonProperty("email")
    @NotEmpty(message = "{Required}")
    @Email(regexp = emailRegex, message = "{Email}")
    private String email;

}
