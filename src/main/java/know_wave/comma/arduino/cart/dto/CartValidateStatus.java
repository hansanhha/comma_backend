package know_wave.comma.arduino.cart.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CartValidateStatus {

    VALID("유효"),
    OVER_MAX_QUANTITY("최대 수량 초과"),
    NOT_ENOUGH_ARDUINO_STOCK("부품 재고 부족"),
    BAD_ARDUINO_STATUS("담을 수 없는 재고 상태");
    
    private final String status;

    public boolean isNotValid() {
        return this != VALID;
    }

    public boolean isOverMaxQuantity() {
        return this == OVER_MAX_QUANTITY;
    }

    public boolean isNotEnoughStock() {
        return this == NOT_ENOUGH_ARDUINO_STOCK;
    }

    public boolean isBadArduinoStatus() {
        return this == BAD_ARDUINO_STATUS;
    }
}
