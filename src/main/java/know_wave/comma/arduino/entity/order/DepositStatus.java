package know_wave.comma.arduino.entity.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DepositStatus {

    REQUIRED("보증금 제출 필요"),
    PAID("보증금 제출"),
    REFUND("보증금 반환"),
    COLLECT("보증금 회수");

    private final String status;
}
