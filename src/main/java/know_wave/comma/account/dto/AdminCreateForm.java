package know_wave.comma.account.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class AdminCreateForm {

    @NotEmpty(message = "{Required}")
    @Length(min = 4, max = 255, message = "{Length}")
    private String accountId;
    @NotEmpty(message = "{Required}")
    @Length(min = 8, max = 255, message = "{Length}")
    private String password;
    @NotEmpty(message = "{Required}")
    @Length(min = 2, max = 8, message = "{Length}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
