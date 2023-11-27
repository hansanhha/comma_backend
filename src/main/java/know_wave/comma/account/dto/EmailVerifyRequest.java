package know_wave.comma.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import static know_wave.comma.common.util.StringStorage.emailRegex;

public class EmailVerifyRequest {

    @NotEmpty(message = "{Required}")
    @Email(regexp = emailRegex,
            message = "{Email.email}")
    private String email;

    @NotEmpty(message = "{Required}")
    @Range(min = 111111, max = 999999, message = "{Range.code}")
    private String code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
