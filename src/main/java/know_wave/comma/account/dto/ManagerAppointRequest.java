package know_wave.comma.account.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class ManagerAppointRequest {

    @NotEmpty(message = "{Required}")
    @Length(min = 4, max = 255, message = "{Length}")
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
