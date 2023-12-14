package know_wave.comma.account.dto;

import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.AcademicStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import static know_wave.comma.common.entity.regexPattern.emailRegex;

public class AccountCreateForm {

    public AccountCreateForm() {
    }

    public AccountCreateForm(String accountId, String password, String name, String email, String academicNumber, AcademicMajor major, AcademicStatus status) {
        this.accountId = accountId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.academicNumber = academicNumber;
        this.major = major;
        this.status = status;
    }

    @NotEmpty(message = "{Required}")
    @Length(min = 4, max = 255, message = "{Length}")
    private String accountId;

    @NotEmpty(message = "{Required}")
    @Length(min = 8, max = 255, message = "{Length}")
    private String password;

    @NotEmpty(message = "{Required}")
    @Length(min = 2, max = 8, message = "{Length.minMax}")
    private String name;

    @NotEmpty(message = "{Required}")
    @Email(regexp = emailRegex,
            message = "{Email.email}")
    private String email;

    @NotEmpty(message = "{Required}")
    @Range(min = 20000000, max = 20300000, message = "{Range.academicNumber}")
    private String academicNumber;

    @NotNull(message = "{Required}")
    private AcademicMajor major;

    @NotNull(message = "{Required}")
    private AcademicStatus status;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcademicNumber() {
        return academicNumber;
    }

    public void setAcademicNumber(String academicNumber) {
        this.academicNumber = academicNumber;
    }

    public AcademicMajor getMajor() {
        return major;
    }

    public void setMajor(AcademicMajor major) {
        this.major = major;
    }

    public AcademicStatus getStatus() {
        return status;
    }

    public void setStatus(AcademicStatus status) {
        this.status = status;
    }
}
