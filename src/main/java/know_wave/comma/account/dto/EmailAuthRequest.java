package know_wave.comma.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import static know_wave.comma.notification.alarm.util.regexPattern.emailRegex;

public class EmailAuthRequest {

    @NotEmpty(message = "{Required}")
    @Email(regexp = emailRegex, message = "{Email}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
