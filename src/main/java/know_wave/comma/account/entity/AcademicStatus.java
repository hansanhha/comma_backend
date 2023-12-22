package know_wave.comma.account.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcademicStatus {

    Enrolled("재학"),
    OnLeave("휴학"),
    Graduate("졸업");

    private final String status;

}
