package know_wave.comma.account.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountStatus {

    ACTIVE("활동 중"),
    INACTIVE("비활성화됨"),
    DELETED("삭제됨"),
    REPORTED("신고됨"),
    BLOCKED("정지됨");

    private final String status;
}
