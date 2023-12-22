package know_wave.comma.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import static know_wave.comma.common.entity.regexPattern.emailRegex;
import static know_wave.comma.common.entity.regexPattern.onlyPhoneNumberRegex;

@Getter
public class AccountCreateForm {

    @JsonProperty("account_id")
    @NotEmpty(message = "{Required}")
    @Length(min = 4, max = 255, message = "{Length}")
    private String accountId;

    @JsonProperty("password")
    @NotEmpty(message = "{Required}")
    @Length(min = 8, max = 255, message = "{Length}")
    private String password;

    @JsonProperty("phone")
    @Length(min = 11, max = 11, message = "{Length}")
    @Pattern(regexp = onlyPhoneNumberRegex, message = "{PhonePattern}")
    private String phone;

    @JsonProperty("name")
    @NotEmpty(message = "{Required}")
    @Length(min = 2, max = 8, message = "{Length.minMax}")
    private String name;

    @JsonProperty("email")
    @NotEmpty(message = "{Required}")
    @Email(regexp = emailRegex,
            message = "{Email.email}")
    private String email;

    @JsonProperty("academic_number")
    @NotEmpty(message = "{Required}")
    @Range(min = 20000000, max = 20300000, message = "{Range.academicNumber}")
    private String academicNumber;

    @JsonProperty("major")
    @NotNull(message = "{Required}")
    private String major;

}
