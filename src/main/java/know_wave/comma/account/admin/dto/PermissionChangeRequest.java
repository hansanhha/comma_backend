package know_wave.comma.account.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PermissionChangeRequest {

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("permission")
    private String role;

}
