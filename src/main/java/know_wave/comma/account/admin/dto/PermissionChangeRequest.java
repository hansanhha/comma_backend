package know_wave.comma.account.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class PermissionChangeRequest {

    @NotEmpty(message = "{required}")
    @JsonProperty("account_id")
    private String accountId;

    @NotEmpty(message = "{required}")
    @JsonProperty("permission")
    private String role;

}
