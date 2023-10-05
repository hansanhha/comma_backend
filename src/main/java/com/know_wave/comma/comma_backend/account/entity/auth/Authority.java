package com.know_wave.comma.comma_backend.account.entity.auth;

public enum Authority {

    ADMIN_READ("READ(ADMIN)"),
    ADMIN_UPDATE("UPDATE(ADMIN)"),
    ADMIN_CREATE("CREATE(ADMIN)"),
    ADMIN_DELETE("DELETE(ADMIN)"),
    ADMIN_APPOINT("APPOINT(ADMIN)"),
    MEMBER_READ("READ(MEMBER)"),
    MEMBER_UPDATE("UPDATE(MEMBER)"),
    MEMBER_CREATE("CREATE(MEMBER)"),
    MEMBER_DELETE("DELETE(MEMBER)"),
    MEMBER_EQUIPMENT_APPLY("EQUIPMENT_APPLY(MEMBER)");

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
