package know_wave.comma.arduino.entity.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Subject {

    SENIER_PROJECT("졸업작품"),
    IOT_PROGRAMMING("IOT 프로그래밍");

    private final String subjectName;
}
