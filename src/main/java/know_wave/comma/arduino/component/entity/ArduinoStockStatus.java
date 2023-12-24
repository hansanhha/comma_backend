package know_wave.comma.arduino.component.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArduinoStockStatus {

    NONE("없음"),
    MORE_THAN_10("10개 이상"),
    LESS_THAN_10("10개 미만"),
    UP_COMMING("입고 예정");

    private final String status;
}
