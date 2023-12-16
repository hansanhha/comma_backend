package know_wave.comma.common.security.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {

    ADMIN_READ("READ(ADMIN)"),
    ADMIN_UPDATE("UPDATE(ADMIN)"),
    ADMIN_CREATE("CREATE(ADMIN)"),
    ADMIN_DELETE("DELETE(ADMIN)"),
    ADMIN_MANAGE_MEMBER("MANAGE_MEMBER(ADMIN)"),
    MANAGER_MANAGE_MEMBER("MANAGE_MEMBER(MANAGER)"),
    MEMBER_READ("READ(MEMBER)"),
    MEMBER_UPDATE("UPDATE(MEMBER)"),
    MEMBER_CREATE("CREATE(MEMBER)"),
    MEMBER_DELETE("DELETE(MEMBER)"),
    MEMBER_ARDUINO_ORDER("ARDUINO_ORDER(MEMBER)");

    private final String authority;
}
