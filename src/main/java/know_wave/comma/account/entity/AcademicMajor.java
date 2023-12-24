package know_wave.comma.account.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcademicMajor {
    SoftwareEngineering("컴퓨터소프트웨어공학과"),
    informationEngineering("컴퓨터정보공학과"),
    AIEngineering("인공지능소프트웨어학과");

    private final String major;

}
