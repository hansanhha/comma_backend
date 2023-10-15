package know_wave.comma.account.entity.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    MEMBER(
        Set.of(
                Authority.MEMBER_READ,
                Authority.MEMBER_CREATE,
                Authority.MEMBER_UPDATE,
                Authority.MEMBER_DELETE,
                Authority.MEMBER_EQUIPMENT_APPLY
        ), "회원"
    ),
    MEMBER_NoEquipmentApply(
            Set.of(
                    Authority.MEMBER_READ,
                    Authority.MEMBER_CREATE,
                    Authority.MEMBER_UPDATE,
                    Authority.MEMBER_DELETE
            ), "회원(실습재료 신청 권한 없음)"
    ),
    MEMBER_NoEquipmentApplyAndCUD(
            Set.of(
                    Authority.MEMBER_READ
            ), "회원(실습재료 신청, 작성 권한 없음)"
    ),
    MANAGER(
        Set.of(
                Authority.ADMIN_READ,
                Authority.ADMIN_CREATE,
                Authority.ADMIN_UPDATE,
                Authority.ADMIN_DELETE,
                Authority.MEMBER_EQUIPMENT_APPLY
        ), "근로 학생"
    ),
    ADMIN(
        Set.of(
                Authority.ADMIN_READ,
                Authority.ADMIN_CREATE,
                Authority.ADMIN_UPDATE,
                Authority.ADMIN_DELETE,
                Authority.ADMIN_APPOINT
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
