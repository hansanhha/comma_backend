package know_wave.comma.community.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentStatus {

    CREATED("작성됨"),
    UPDATED("수정됨"),
    DELETED("삭제됨"),
    REPORTED("신고됨"),
    DELETE_BY_ADMIN("관리자에 의해 삭제됨");

    private final String status;
}
