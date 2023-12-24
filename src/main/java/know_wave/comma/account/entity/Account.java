package know_wave.comma.account.entity;

import jakarta.persistence.*;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.config.security.entity.Authority;
import know_wave.comma.config.security.entity.Role;
import know_wave.comma.config.security.entity.SecurityAccount;
import know_wave.comma.config.security.entity.Token;
import know_wave.comma.common.entity.BaseTimeEntity;
import know_wave.comma.common.notification.push.entity.PushNotificationOption;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity implements Persistable<String> {

    public static Account create(String id, String email, String name, String password, String phoneNumber, String academicNumber, AcademicMajor academicMajor) {
        return Account.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .academicNumber(academicNumber)
                .academicMajor(academicMajor)
                .academicStatus(AcademicStatus.Enrolled)
                .role(Role.MEMBER)
                .accountStatus(AccountStatus.ACTIVE)
                .notificationOption(PushNotificationOption.create())
                .build();
    }

    public static Account createWithoutPhone(String id, String email, String name, String password, String academicNumber, AcademicMajor academicMajor) {
        return Account.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .academicNumber(academicNumber)
                .academicMajor(academicMajor)
                .academicStatus(AcademicStatus.Enrolled)
                .role(Role.MEMBER)
                .accountStatus(AccountStatus.ACTIVE)
                .notificationOption(PushNotificationOption.create())
                .build();
    }

    @Id
    @Column(name = "account_id")
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, length = 8)
    private String name;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(length = 11)
    private String phoneNumber;

    @Column(nullable = false)
    private String academicNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicMajor academicMajor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicStatus academicStatus;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    private String profileImage;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "push_notification_option_id")
    private PushNotificationOption notificationOption;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderList;
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
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
