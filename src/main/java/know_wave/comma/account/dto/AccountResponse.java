package know_wave.comma.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.account.entity.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountResponse {

    public static AccountResponse to(Account account) {
        return new AccountResponse(
                account.getName(),
                account.getPhoneNumber(),
                account.getEmail(),
                account.getAcademicNumber(),
                account.getAcademicStatus().getStatus(),
                account.getAcademicMajor().getMajor(),
                account.getRole().getRole()
        );
    }

    @JsonProperty("name")
    private final String name;
    @JsonProperty("phone")
    private final String phone;
    @JsonProperty("email")
    private final String email;
    @JsonProperty("academic_number")
    private final String academicNumber;
    @JsonProperty("academic_status")
    private final String academicStatus;
    @JsonProperty("academic_major")
    private final String academicMajor;
    @JsonProperty("permission")
    private final String permission;
}
