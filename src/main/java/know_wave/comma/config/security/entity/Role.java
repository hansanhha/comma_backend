package know_wave.comma.config.security.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
    MEMBER(
        Set.of(
                Authority.MEMBER_READ,
                Authority.MEMBER_CREATE,
                Authority.MEMBER_UPDATE,
                Authority.MEMBER_DELETE,
                Authority.MEMBER_ARDUINO_ORDER
        ), "회원"
    ),
    MEMBER_EXCLUDE_ARDUINO_ORDER(
            Set.of(
                    Authority.MEMBER_READ,
                    Authority.MEMBER_CREATE,
                    Authority.MEMBER_UPDATE,
                    Authority.MEMBER_DELETE
            ), "회원(실습재료 신청 제한)"
    ),
    MEMBER_EXCLUDE_ARDUINO_ORDER_COMMUNITY(
            Set.of(
                    Authority.MEMBER_READ
            ), "회원(실습재료 신청, 커뮤니티 컨텐츠 작성 제한)"
    ),
    MANAGER(
        Set.of(
                Authority.ADMIN_READ,
                Authority.ADMIN_CREATE,
                Authority.ADMIN_UPDATE,
                Authority.ADMIN_DELETE,
                Authority.MEMBER_ARDUINO_ORDER,
                Authority.MANAGER_MANAGE_MEMBER
        ), "근로 학생"
    ),
    ADMIN(
        Set.of(
                Authority.ADMIN_READ,
                Authority.ADMIN_CREATE,
                Authority.ADMIN_UPDATE,
                Authority.ADMIN_DELETE,
                Authority.ADMIN_MANAGE_MEMBER
        ), "관리자"
    );

    private final Set<Authority> authorities;
    private final String grade;

    public Set<Authority> getPermissions() {
        return authorities;
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
