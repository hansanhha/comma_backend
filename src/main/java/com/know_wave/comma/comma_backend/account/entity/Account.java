package com.know_wave.comma.comma_backend.account.entity;

import com.know_wave.comma.comma_backend.account.entity.auth.Authority;
import com.know_wave.comma.comma_backend.order.entity.OrderInfo;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import com.know_wave.comma.comma_backend.account.entity.auth.Role;
import com.know_wave.comma.comma_backend.account.entity.token.Token;
import com.know_wave.comma.comma_backend.security.auth.SecurityAccount;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Entity
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

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokenList;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderInfo> orderList;
    
    public UserDetails toUserDetails() {
        return new SecurityAccount(this);
    }
    
    public boolean dontHaveAuthority(Authority authority) {
        return !role.getPermissions().contains(authority);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Builder() {}

        private String id = "";
        private String password = "";
        private Role role = Role.MEMBER;

        private String email = "";
        private String name = "";
        private AcademicStatus academicStatus = null;
        private String academicNumber = "";
        private AcademicMajor academicMajor = null;

        public Builder id(String id) {
            this.id = id;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Builder role(Role role) {
            this.role = role;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder academicStatus(AcademicStatus academicStatus) {
            this.academicStatus = academicStatus;
            return this;
        }

        public Builder academicNumber(String academicNumber) {
            this.academicNumber = academicNumber;
            return this;
        }

        public Builder academicMajor(AcademicMajor major) {
            this.academicMajor = major;
            return this;
        }

        public Account build() {
            return new Account(id, email, name, password, academicNumber, academicMajor, academicStatus, role);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAcademicNumber() {
        return academicNumber;
    }

    public String getAcademicMajor() {
        return academicMajor.getMajor();
    }

    public String getAcademicStatus() {
        return academicStatus.getStatus();
    }

    public Role getRole() {
        return role;
    }

    public List<Token> getTokenList() {
        return tokenList;
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
