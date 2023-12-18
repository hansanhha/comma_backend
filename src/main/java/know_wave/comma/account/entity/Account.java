package know_wave.comma.account.entity;

import jakarta.persistence.*;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.config.security.entity.Authority;
import know_wave.comma.config.security.entity.Role;
import know_wave.comma.config.security.entity.SecurityAccount;
import know_wave.comma.config.security.entity.Token;
import know_wave.comma.common.entity.BaseTimeEntity;
import know_wave.comma.common.notification.push.entity.PushNotificationOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Account extends BaseTimeEntity implements Persistable<String> {

    protected Account() {}

    public Account(String id, String email, String name, String password, String academicNumber, AcademicMajor academicMajor, AcademicStatus academicStatus) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.academicNumber = academicNumber;
        this.academicMajor = academicMajor;
        this.academicStatus = academicStatus;
        this.role = Role.MEMBER;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public Account(String id, String email, String name, String password, String academicNumber, AcademicMajor academicMajor, AcademicStatus academicStatus, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.academicNumber = academicNumber;
        this.academicMajor = academicMajor;
        this.academicStatus = academicStatus;
        this.role = role;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    @Id
    @Column(name = "account_id")
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, length = 8)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String academicNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicMajor academicMajor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicStatus academicStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    private String profileImage;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private PushNotificationOption alarmOption;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokenList;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderList;
    
    public UserDetails toUserDetails() {
        return new SecurityAccount(this);
    }
    
    public boolean dontHaveAuthority(Authority authority) {
        return !role.getPermissions().contains(authority);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Account &&
                ((Account) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
