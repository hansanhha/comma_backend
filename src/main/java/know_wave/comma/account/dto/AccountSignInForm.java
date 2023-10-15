package know_wave.comma.account.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class AccountSignInForm {

    @NotEmpty(message = "{Required}")
    @Length(min = 4, max = 255, message = "{Length.min}")
    private String accountId;

    @NotEmpty(message = "{Required}")
    @Length(min = 8, max = 255, message = "{Length.min}")
    private String password;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
