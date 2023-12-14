package know_wave.comma.arduino.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DepositStatus {
    
    REQUIRED("보증금 결제 필요"),
    PAID("보증금 결제"),
    RETURN("보증금 반환"),
    RETURN_SCHEDULED("보증금 반환 예정"),
    RECLAIMED("보증금 회수");

    private final String status;
}
