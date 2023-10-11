package com.know_wave.comma.comma_backend.account.entity.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.know_wave.comma.comma_backend.account.entity.auth.Authority.*;

public enum Role {
    MEMBER(
        Set.of(
                MEMBER_READ,
                MEMBER_CREATE,
                MEMBER_UPDATE,
                MEMBER_DELETE,
                MEMBER_EQUIPMENT_APPLY
        ), "회원"
    ),
    MEMBER_NoEquipmentApply(
            Set.of(
                    MEMBER_READ,
                    MEMBER_CREATE,
                    MEMBER_UPDATE,
                    MEMBER_DELETE
            ), "회원(실습재료 신청 권한 없음)"
    ),
    MEMBER_NoEquipmentApplyAndCUD(
            Set.of(
                    MEMBER_READ
            ), "회원(실습재료 신청, 작성 권한 없음)"
    ),
    MANAGER(
        Set.of(
                ADMIN_READ,
                ADMIN_CREATE,
                ADMIN_UPDATE,
                ADMIN_DELETE,
                MEMBER_EQUIPMENT_APPLY
        ), "근로 학생"
    ),
    ADMIN(
        Set.of(
                ADMIN_READ,
                ADMIN_CREATE,
                ADMIN_UPDATE,
                ADMIN_DELETE,
                ADMIN_APPOINT
        ), "관리자"
    );

    private final Set<Authority> authorities;
    private final String grade;

    Role(Set<Authority> authorities, String grade) {
        this.authorities = authorities;
        this.grade = grade;
    }

    public Set<Authority> getPermissions() {
        return authorities;
    }

    public String getGrade() {
        return grade;
    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = getPermissions()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
